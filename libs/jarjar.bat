java -jar jarjar.jar process ../rules/latest.rules minecraft_server.jar t1.jar
java -jar jarjar.jar process ../rules/namespace.rules t1.jar t2.jar
java -jar retroguard.jar t2.jar minecraft_servero.jar ../rules/latest.rgs t3.log
del t1.jar
del t2.jar
del t3.log
pause
