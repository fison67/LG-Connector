/**
 *  LG Code Zero v2(v.0.0.1)
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

metadata {
	definition (name: "LG Code Zero v2", namespace: "fison67", author: "fison67") {
        capability "Switch"
        
        attribute "status", "string"
        attribute "cleanMode", "string"
        attribute "batteryLevel", "string"
	}

	simulator {
	}
    
	preferences {
        input name: "language", title:"Select a language" , type: "enum", required: true, options: ["EN", "KR"], defaultValue: "KR", description:"Language for DTH"
	}

	tiles(scale: 2) {
		
        multiAttributeTile(name:"switch", type: "generic", width: 6, height: 2){
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
              attributeState "on", label:'${name}',  backgroundColor:"#00a0dc", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-dehumidifier-off.png?raw=true"
                attributeState "off", label:'${name}', backgroundColor:"#ffffff", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-dehumidifier-off.png?raw=true"
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

def getStateStr(state){
	if(language == "EN"){
    	return state
    }else{
        if(state == "STANDBY"){
            return "대기 중"
        }else if(state == "RUNNING"){
            return "청소 중"
        }else if(state == "CHARGING"){
            return "충전 중"
        }else if(state == "CHARGING_COMPLETE"){
            return "충전 완료"
        }else{
            return state
        }
    }
}

def getCleanModeStr(state){
	if(language == "EN"){
    	return state
    }else{
        if(state == "IGNORE"){
            return "IGNORE"
        }else if(state == "OFF"){
            return "꺼짐"
        }else if(state == "NORMAL"){
            return "표준"
        }else if(state == "HIGH"){
            return "강"
        }else if(state == "TURBO"){
            return "터보"
        }else if(state == "MOP"){
            return "물걸레"
        }else if(state == "AUTO"){
            return "오토"
        }else{
            return state
        }
    }
}

def getBatteryStr(state){
	if(language == "EN"){
    	return state
    }else{
        if(state == "HIGH"){
            return "상"
        }else if(state == "MID"){
            return "중"
        }else if(state == "LOW"){
            return "하"
        }else if(state == "HIGH"){
            return "강"
        }else if(state == "WARNING"){
            return "경고"
        }else if(state == "IGNORE"){
            return "ignore"
        }else{
            return state
        }
    }
}

def setStatus(data){
	log.debug "Update >> ${data.key} >> ${data.data}"
    def jsonObj = new JsonSlurper().parseText(data.data)
    
     if(jsonObj.data.state.reported != null){
    	def report = jsonObj.data.state.reported
        if(report["qmState"] != null){
        	def qmState = report.qmState
            if(qmState["monStatus"] != null){
        		sendEvent(name: "switch", value: (qmState["monStatus"] == "RUNNING" ? "off" : "on"))
        		sendEvent(name: "status", value: getStateStr(qmState["monStatus"]))
            }
            
            if(qmState["cleanMode"] != null){
        		sendEvent(name: "cleanMode", value: getCleanModeStr(qmState["cleanMode"]))
            }
            if(qmState["batteryLevel"] != null){
        		sendEvent(name: "batteryLevel", value: getBatteryStr(qmState["batteryLevel"]))
            }
        }
     }
    
    
    updateLastTime();
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now, displayed:false)
}

def on(){

}

def off(){

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
        "path": "/devices/control2",
        "headers": [
        	"HOST": state.app_url,
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
