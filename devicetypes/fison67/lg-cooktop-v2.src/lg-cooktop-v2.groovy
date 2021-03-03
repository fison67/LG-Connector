/**
 *  LG Cooktop v2(v.0.0.1)
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
	definition (name: "LG Cooktop v2", namespace: "fison67", author: "fison67", ocfDeviceType: "oic.d.oven") {
        capability "Switch"
        
        command "setStatus"
	}

	simulator {}
	preferences {}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def installed(){
    state.power1 = 0
    state.power2 = 0
    state.power3 = 0
    sendEvent(name:"switch", value:"off")
}

def updated() {}

def setInfo(String app_url, String address) {
	log.debug "${app_url}, ${address}"
	state.app_url = app_url
    state.id = address
    
}

def on(){

}

def off(){

}

def setStatus(data){
	log.debug "Update >> ${data.data}"
    
    def jsonObj = new JsonSlurper().parseText(data.data)
    if(jsonObj.data.state.reported != null){
    	def report = jsonObj.data.state.reported
        
        if(report.cooktopState != null){
            if(report.cooktopState["cooktopPowerLevel_1_1"] != null){
            	state.power1 = report.cooktopState["cooktopPowerLevel_1_1"]
            }
            if(report.cooktopState["cooktopPowerLevel_1_2"] != null){
            	state.power2 = report.cooktopState["cooktopPowerLevel_1_2"]
            }
            if(report.cooktopState["cooktopPowerLevel_3_2"] != null){
            	state.power3 = report.cooktopState["cooktopPowerLevel_3_2"]
            }
            sendEvent(name:"switch", value: ((state.power1 as int) > 0 || (state.power2 as int) > 0 || (state.power3 as int) > 0) ? "on" :"off")
            
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
