/**
 *  LG Robot Cleaner v2 (v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2021 fison67@nate.com
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
	definition (name: "LG Robot Cleaner v2", namespace: "fison67", author: "fison67", ocfDeviceType:"oic.d.robotcleaner") {
        capability "Switch"
        capability "Robot Cleaner Movement"
	}

	simulator {}
	preferences {}
}

// parse events into attributes
def parse(String description) {}

def installed(){
	sendEvent(name:"switch", value: "off")
    sendEvent(name:"robotCleanerMovement", value:"powerOff")
}

def updated() {}

def setInfo(String app_url, String address) {
	log.debug "${app_url}, ${address}"
	state.app_url = app_url
    state.id = address
}

def getRobotCleanModeValue(mode){
	if(mode == "CHARGING" || mode == "HOMING"){
    	return mode.toLowerCase()
    }else if(mode == "INITAILIZING" || mode == "STANDBY" || mode == "PAUSE" || mode == "PAUSE_EDGE" || mode == "PAUSE_ZIGZAG" || mode == "PAUSE_SELECT" || mode == "PAUSE_LEARNING"){
    	return "idle"
    }else if(mode == "SLEEP"){
    	return "powerOff"
    }else if(mode == "DOCKING"){

    }else if(mode == "CLEAN_EDGE" || mode == "CLEAN_SPIRAL" || mode == "CLEAN_SELECT" || mode == "CLEAN_SPECIFIED" || mode == "CLEAN_ZIGZAG" || mode == "CLEAN_LEARNING"){
    	return "cleaning"
    }
    return null
}

def setStatus(data){
	log.debug "Update >> ${data.key} >> ${data.data}"
    def jsonObj = new JsonSlurper().parseText(data.data)
    if(jsonObj.data.state.reported != null){
    	def report = jsonObj.data.state.reported
        
        if(report["ROBOT_STATE"] != null){
        	def mode = getRobotCleanModeValue(report["ROBOT_STATE"])
			if(mode){
            	sendEvent(name:"robotCleanerMovement", value: mode)
            }
            sendEvent(name:"switch", value: report["ROBOT_STATE"] != "SLEEP" ? "on" : "off")
        }
    }
    
}

def setRobotCleanerMovement(movement){
	log.debug "setRobotCleanerMovement: " + movement
	if(movemoent == "charging"){
    	makeCommand('', '{"command":"Set","ctrlKey":"basicCtrl","dataKey":"HOMING"}')
    }else if(movement == "powerOff" || movement == "idle"){
    	off()
    }else if(movement == "cleaning"){
    	on()
    }
}

def on(){
    makeCommand('', '{"command":"Set","ctrlKey":"basicCtrl","dataKey":"CLEAN_START"}')
}

def off(){
    makeCommand('', '{"command":"Set","ctrlKey":"basicCtrl","dataKey":"PAUSE"}')
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
