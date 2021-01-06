# Performance_Ele

command to start hub
java -jar selenium-server-standalone-3.141.59.jar -role hub -timeout 0

To start the node
java -Dwebdriver.chrome.driver="C:\chromedriver.exe" -jar selenium-server-standalone-3.141.59.jar -role webdriver -hub http://192.168.0.102:4444/grid/register/
