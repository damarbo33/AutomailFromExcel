# AutomailFromExcel
AutomailFromExcel is an utility to read an excel report that contains delivery dates in a software enterprise,
and send mail to people to remember they must advance the status of their deliverys.

## Dependencies
You must download the following libraries:
  - Apache POI 3.15: To read excel files. http://poi.apache.org/download.html
  - JavaMail 1.4.5: To send emails. http://www.oracle.com/technetwork/java/javamail/javamail145-1904579.html

## Configuration
In the executable's path there should be a file named "config.properties" with the following lines to connect to an smtp server:

server=127.0.0.1
username=domain\\user
password=xxxxxx
smptPort=4125
from=name.example@domain.com

