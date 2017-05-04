# cd C:/Program Files/IBM/WebSphere/AppServer/profiles/AppSrv01/bin
# ./wsadmin -lang jython -user websphere -password websphere -conntype SOAP -f D:/p/jee/jee-ear/deploy.py
import sys
from com.ibm.ws.scripting import ScriptingException

AdminApp.update('jee-ear', 'app', '[-operation update -contents D:/p/jee/jee-ear/target/jee.ear -usedefaultbindings]') 

AdminConfig.save()
