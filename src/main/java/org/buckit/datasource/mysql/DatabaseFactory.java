/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.buckit.datasource.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.buckit.Config;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DatabaseFactory {
    static Logger log = Logger.getLogger(DatabaseFactory.class.getName());

    public static enum ProviderType {
        MySql, MsSql
    }

    // =========================================================
    // Data Field
    private static DatabaseFactory          instance;
    private static ScheduledExecutorService executor;
    private ProviderType                    providerType;
    private ComboPooledDataSource           source;

    // =========================================================
    // Constructor
    public DatabaseFactory() throws SQLException {
        try {
            if (Config.DATABASE_MAX_CONNECTIONS < 2) {
                Config.DATABASE_MAX_CONNECTIONS = 2;
                log.warning("A minimum of " + Config.DATABASE_MAX_CONNECTIONS + " db connections are required.");
            }

            source = new ComboPooledDataSource();
            source.setAutoCommitOnClose(true);

            source.setInitialPoolSize(10);
            source.setMinPoolSize(10);
            source.setMaxPoolSize(Math.max(10, Config.DATABASE_MAX_CONNECTIONS));

            source.setAcquireRetryAttempts(0); // try to obtain connections
                                               // indefinitely (0 = never quit)
            source.setAcquireRetryDelay(500); // 500 milliseconds wait before
                                              // try to acquire connection again
            source.setCheckoutTimeout(0); // 0 = wait indefinitely for new
                                          // connection
            // if pool is exhausted
            source.setAcquireIncrement(5); // if pool is exhausted, get 5 more
                                           // connections at a time
            // cause there is a "long" delay on acquire connection
            // so taking more than one connection at once will make connection
            // pooling
            // more effective.

            // this "connection_test_table" is automatically created if not
            // already there
            source.setAutomaticTestTable("connection_test_table");
            source.setTestConnectionOnCheckin(false);

            // testing OnCheckin used with IdleConnectionTestPeriod is faster
            // than testing on checkout

            source.setIdleConnectionTestPeriod(3600); // test idle connection
                                                      // every 60 sec
            source.setMaxIdleTime(Config.DATABASE_MAX_IDLE_TIME); // 0 = idle
                                                                  // connections
                                                                  // never
                                                                  // expire
            // *THANKS* to connection testing configured above
            // but I prefer to disconnect all connections not used
            // for more than 1 hour

            // enables statement caching, there is a "semi-bug" in c3p0 0.9.0
            // but in 0.9.0.2 and later it's fixed
            source.setMaxStatementsPerConnection(100);

            source.setBreakAfterAcquireFailure(false); // never fail if any way
                                                       // possible
            // setting this to true will make
            // c3p0 "crash" and refuse to work
            // till restart thus making acquire
            // errors "FATAL" ... we don't want that
            // it should be possible to recover
            source.setDriverClass(Config.DATABASE_DRIVER);
            source.setJdbcUrl(Config.DATABASE_URL);
            source.setUser(Config.DATABASE_LOGIN);
            source.setPassword(Config.DATABASE_PASSWORD);

            /* Test the connection */
            source.getConnection().close();

            if (Config.DATABASE_DRIVER.toLowerCase().contains("microsoft"))
                providerType = ProviderType.MsSql;
            else
                providerType = ProviderType.MySql;
        } catch (SQLException x) {
            // re-throw the exception
            throw x;
        } catch (Exception e) {
            throw new SQLException("Could not init DB connection:" + e.getMessage());
        }
    }

    // =========================================================
    // Method - Public
    public final String prepQuerySelect(String[] fields, String tableName, String whereClause, boolean returnOnlyTopRecord) {
        String msSqlTop1 = "";
        String mySqlTop1 = "";
        if (returnOnlyTopRecord) {
            if (getProviderType() == ProviderType.MsSql)
                msSqlTop1 = " Top 1 ";
            if (getProviderType() == ProviderType.MySql)
                mySqlTop1 = " Limit 1 ";
        }
        String query = "SELECT " + msSqlTop1 + safetyString(fields) + " FROM " + tableName + " WHERE " + whereClause + mySqlTop1;
        return query;
    }

    public void shutdown() {
        try {
            source.close();
        } catch (Exception e) {
            log.log(Level.INFO, "", e);
        }
        try {
            source = null;
        } catch (Exception e) {
            log.log(Level.INFO, "", e);
        }
    }

    public final String safetyString(String... whatToCheck) {
        // NOTE: Use brace as a safty precaution just incase name is a reserved
        // word
        final char braceLeft;
        final char braceRight;

        if (getProviderType() == ProviderType.MsSql) {
            braceLeft = '[';
            braceRight = ']';
        } else {
            braceLeft = '`';
            braceRight = '`';
        }

        int length = 0;

        for (String word : whatToCheck) {
            length += word.length() + 4;
        }

        final StringBuilder sbResult = new StringBuilder(length);

        for (String word : whatToCheck) {
            if (sbResult.length() > 0) {
                sbResult.append(", ");
            }

            sbResult.append(braceLeft);
            sbResult.append(word);
            sbResult.append(braceRight);
        }

        return sbResult.toString();
    }

    // =========================================================
    // Property - Public
    public static DatabaseFactory getInstance() throws SQLException {
        synchronized (DatabaseFactory.class) {
            if (instance == null) {
                instance = new DatabaseFactory();
            }
        }
        return instance;
    }

    public Connection getConnection() // throws SQLException
    {
        Connection con = null;

        while (con == null) {
            try {
                con = source.getConnection();

                getExecutor().schedule(new ConnectionCloser(con, new RuntimeException()), 60, TimeUnit.SECONDS);
            } catch (SQLException e) {
                log.log(Level.WARNING, "DatabaseFactory: getConnection() failed, trying again " + e.getMessage(), e);
            }
        }
        return con;
    }

    private static class ConnectionCloser implements Runnable {
        private Connection       c;
        private RuntimeException exp;

        public ConnectionCloser(Connection con, RuntimeException e) {
            c = con;
            exp = e;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {
                if (!c.isClosed()) {
                    log.log(Level.WARNING, "Unclosed connection! Trace: " + exp.getStackTrace()[1], exp);
                }
            } catch (SQLException e) {
                log.log(Level.WARNING, "", e);
            }

        }
    }

    public static void close(Connection con) {
        if (con == null)
            return;

        try {
            con.close();
        } catch (SQLException e) {
            log.log(Level.WARNING, "Failed to close database connection!", e);
        }
    }

    private static ScheduledExecutorService getExecutor() {
        if (executor == null) {
            synchronized (DatabaseFactory.class) {
                if (executor == null)
                    executor = Executors.newSingleThreadScheduledExecutor();
            }
        }
        return executor;
    }

    public int getBusyConnectionCount() throws SQLException {
        return source.getNumBusyConnectionsDefaultUser();
    }

    public int getIdleConnectionCount() throws SQLException {
        return source.getNumIdleConnectionsDefaultUser();
    }

    public final ProviderType getProviderType() {
        return providerType;
    }
}