# cd C:/Program Files/IBM/WebSphere/AppServer/profiles/AppSrv01/bin
# ./wsadmin -lang jython -user websphere -password websphere -conntype SOAP -f D:/p/jee/jee-ear/deploy.py
import sys
from com.ibm.ws.scripting import ScriptingException

apps = AdminApp.list()
if "jee-ear" in apps:
  AdminApp.uninstall("jee-ear")

options = "-usedefaultbindings"
AdminApp.install("D:/p/jee/jee-ear/target/jee.ear", options)

AdminConfig.save()

appManager = AdminControl.queryNames('type=ApplicationManager,*')
AdminControl.invoke(appManager, 'startApplication', 'jee-ear')

