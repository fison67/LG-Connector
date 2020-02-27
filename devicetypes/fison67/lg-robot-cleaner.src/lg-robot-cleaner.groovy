/**
 *  LG Robot Cleaner(v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2020 fison67@nate.com
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
*/
 
import groovy.json.JsonSlurper
import groovy.transform.Field

metadata {
	definition (name: "LG Robot Cleaner", namespace: "fison67", author: "fison67") {
        capability "Switch"
	}

	simulator {
	}
    
	preferences {
	}

	tiles(scale: 2) {
		
        multiAttributeTile(name:"switch", type: "generic", width: 6, height: 2){
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
              attributeState "on", label:'${name}', action:"switch.off",  backgroundColor:"#00a0dc", nextState:"turningOff", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-dehumidifier-off.png?raw=true"
                attributeState "off", label:'${name}', action:"switch.on", backgroundColor:"#ffffff", nextState:"turningOn", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-dehumidifier-off.png?raw=true"
                
                attributeState "turningOn", label:'${name}', action:"switch.off", backgroundColor:"#00a0dc", nextState:"turningOff", icon:"httpshttps://github.com/fison67/LG-Connector/blob/master/icons/lg-dehumidifier-off.png?raw=true"
                attributeState "turningOff", label:'${name}', action:"switch.on", backgroundColor:"#ffffff", nextState:"turningOn", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-dehumidifier-off.png?raw=true"
			}
            
			tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Updated: ${currentValue}')
            }
		}
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def updated() {}

def setInfo(String app_url, String address) {
	log.debug "${app_url}, ${address}"
	state.app_url = app_url
    state.id = address
}

def setData(dataList){
	for(data in dataList){
        state[data.id] = data.code
    }
}

def setStatus(data){
	log.debug "Update >> ${data.key} >> ${data.data}"
    def jsonObj = new JsonSlurper().parseText(data.data)
    
    updateLastTime();
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now, displayed:false)
}

def on(){
    makeCommand("SetCleanStart", "CLEAN_START")
}

def off(){
	makeCommand("SetHoming", "HOMING")
}

def control(cmd, value){
	makeCommand(cmd, value)
}

def makeCommand(command, value){
    def body = [
        "id": state.id,
        "command": command,
        "value": value
    ]
    def options = _makeCommand(body)
    sendCommand(options, null)
}

def _makeCommand(body){
	def options = [
     	"method": "POST",
        "path": "/devices/control",
        "headers": [
        	"HOST": parent.getServerAddress(),
            "Content-Type": "application/json"
        ],
        "body":body
    ]
    return options
}

def sendCommand(options, _callback){
	def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: _callback])
    sendHubCommand(myhubAction)
}
