/**
 *  LG Cooktop v2(v.0.0.2)
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

metadata {
	definition (name: "LG Ceiling Fan v2", namespace: "fison67", author: "fison67", vid: "986a4dd5-16d2-33f1-98b8-ff0599a2156e", ocfDeviceType: "oic.d.switch") {
        capability "Switch"
		capability "Fan Speed"
        
        command "setStatus"
	}

	simulator {}
	preferences {
        input name: "wind1", title:"Wind#1 Type" , type: "number", required: false, defaultValue: 2
        input name: "wind2", title:"Wind#2 Type" , type: "number", required: false, defaultValue: 4
        input name: "wind3", title:"Wind#3 Type" , type: "number", required: false, defaultValue: 6
        input name: "wind4", title:"Wind#4 Type" , type: "number", required: false, defaultValue: 7
    }
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def installed(){
	state.prvFanSpeed = 0
    sendEvent(name:"switch", value:"off")
}

def updated() {
    sendEvent(name:"fanSpeed", value: 1)
}

def setInfo(String app_url, String address) {
	log.debug "${app_url}, ${address}"
	state.app_url = app_url
    state.id = address
}

def on(){
	_processFanSpeed("on")
    makeCommand('', '{"command":"Set","ctrlKey":"basicCtrl","dataKey":"airState.operation","dataValue":"1"}')
}

def off(){
	_processFanSpeed("off")
    makeCommand('', '{"command":"Set","ctrlKey":"basicCtrl","dataKey":"airState.operation","dataValue":"0"}')
}

def setFanSpeed(speed){
	if(speed == 0){
		off()
    }else if(speed == 1){
		wind(wind1 == null ? 2 : wind1)
    }else if(speed == 2){
		wind(wind2 == null ? 4 : wind2)
    }else if(speed == 3){
		wind(wind3 == null ? 6 : wind3)
    }else if(speed == 4){
		wind(wind4 == null ? 7 : wind4)
    }
}

def _processFanSpeed(power){
	if(power == "on"){
    	sendEvent(name: 'fanSpeed', value: (state.prvFanSpeed as int))
    }else if(power == "off"){
		state.prvFanSpeed = device.currentValue('fanSpeed')
    	sendEvent(name:"fanSpeed", value: 0)
    }
}

def wind(val){
	makeCommand('', '{"command":"Set","ctrlKey":"basicCtrl","dataKey":"airState.windStrength","dataValue":' + val + '}')
}

def setStatus(data){
	log.debug "Update >> ${data.data}"
    
    def jsonObj = new JsonSlurper().parseText(data.data)
    if(jsonObj.data.state.reported != null){
    	def report = jsonObj.data.state.reported
        
        if(report["airState.operation"] != null){
        	def power = report["airState.operation"] == 1 ? "on" : "off"
            sendEvent(name:"switch", value: power)
            _processFanSpeed(power)
        }
        
        if(report["airState.windStrength"] != null){
            def fanSpeed = report["airState.windStrength"]
            def speed = 0
            if(fanSpeed == wind1){
                speed = 1
            }else if(fanSpeed == wind2){
                speed = 2
            }else if(fanSpeed == wind3){
                speed = 3
            }else if(fanSpeed == wind4){
                speed = 4
            }
            sendEvent(name:"fanSpeed", value: speed)
        }
    }
    
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
