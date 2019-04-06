/**
 *  LG Mini Warsher(v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2018 fison67@nate.com
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

@Field
STATE_VALUE = [
    0: [val: "@WM_STATE_POWER_OFF_W", str: ["EN":"POWER OFF", "KR":"전원 OFF"] ],
    1: [val: "@WM_STATE_INITIAL_W", str: ["EN":"INITIAL", "KR":"대기 중"] ],
    2: [val: "@WM_STATE_PAUSE_W", str: ["EN":"PAUSE", "KR":"일시정지 중"] ],
    3: [val: "@WM_STATE_DETECTING_W", str: ["EN":"DETECTING", "KR":"옷감량 확인 중"] ],
    4: [val: "@WM_STATE_SOAK_W", str: ["EN":"ERROR", "KR":"불림 중"] ],
    5: [val: "@WM_STATE_RUNNING_W", str: ["EN":"RUNNING", "KR":"세탁 중"] ],
    6: [val: "@WM_STATE_RINSING_W", str: ["EN":"RINSING", "KR":"헹굼 중"] ],
    7: [val: "@WM_STATE_SPINNING_W", str: ["EN":"SPINNING", "KR":"탈수 중"] ],
    8: [val: "@WM_STATE_COMPLETE_W", str: ["EN":"COMPLET", "KR":"세탁 완료"] ],
    9: [val: "@WM_STATE_RESERVE_W", str: ["EN":"RESERVE", "KR":"예약 중"] ],
    10: [val: "@WM_STATE_FIRMWARE_W", str: ["EN":"FIRMWARE", "KR":"업데이트 중"] ],
    11: [val: "@WM_STATE_SMART_DIAGNOSIS_W", str: ["EN":"SMART DIAGNOSIS", "KR":"스마트 진단 중"] ]
]

metadata {
	definition (name: "LG Mini Washer", namespace: "fison67", author: "fison67") {
        capability "Sensor"
        capability "Switch"
        capability "Switch Level"
        capability "Configuration"
        
        command "setStatus"
        
        attribute "leftMinute", "number"
        attribute "rinseCount", "number"
        attribute "prvState", "string"
        attribute "curState", "string"
        attribute "waterTemp", "string"
        attribute "spinSpeed", "string"
        
	}

	simulator {
	}
    
	preferences {
        input name: "language", title:"Select a language" , type: "enum", required: true, options: ["EN", "KR"], defaultValue: "KR", description:"Language for DTH"
	}

	tiles(scale: 2) {
		
        multiAttributeTile(name:"curState", type: "generic", width: 6, height: 2){
			tileAttribute ("device.curState", key: "PRIMARY_CONTROL") {
             	attributeState("default", label:'${currentValue}', backgroundColor:"#00a0dc", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-washer.png?raw=true")
			}
            
			tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Updated: ${currentValue}')
            }
            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
                attributeState "level", action:"setLevel"
            }     
		}
        
        valueTile("temp_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Water Temp'
        }
        valueTile("waterTemp", "device.waterTemp", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("spinSpeed_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Spin Speed'
        }
        valueTile("spinSpeed", "device.spinSpeed", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("rinseCount_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Rinse Count'
        }
        valueTile("rinseCount", "device.rinseCount", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        
        
        valueTile("leftTime_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Left Time'
        }
        valueTile("leftTime", "device.leftTime", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def updated() {
}

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
    
    if(jsonObj.State != null){
    	if(jsonObj.State.value == "0"){
        	sendEvent(name:"switch", value: "off")
        }else{
        	sendEvent(name:"switch", value: "on")
        }
        sendEvent(name:"curState", value: STATE_VALUE[jsonObj.State.value as int]["str"][language])
    }
    
    if(jsonObj.Remain_Time_H != null){
    	state.remainTimeH = changeTime(jsonObj.Remain_Time_H.rValue)
	}
    if(jsonObj.Remain_Time_M != null){
    	state.remainTimeM = changeTime(jsonObj.Remain_Time_M.rValue)
	}
    
    if(jsonObj.RinseCount != null){
    	sendEvent(name:"rinseCount", value: jsonObj.RinseCount.value)
	}
    if(jsonObj.WTemp != null){
    	sendEvent(name:"waterTemp", value: jsonObj.WTemp.sValue)
	}
    if(jsonObj.SpinSpeed != null){
    	sendEvent(name:"spinSpeed", value: jsonObj.SpinSpeed.rValue)
	}
    
    
    sendEvent(name:"leftTime", value: state.remainTimeH + ":" + state.remainTimeM + ":00")
    if(jsonObj.Remain_Time_H != null){
    	sendEvent(name:"leftMinute", value: jsonObj.Remain_Time_H.value * 60 + jsonObj.Remain_Time_M.value)
    }

    updateLastTime();
}

def changeTime(time){
	if(time < 10){
    	return "0" + time
    }
    return "" + time
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now, displayed:false)
}

def on(){}

def off(){}

def makeCommand(type, value){
    def body = [
        "id": state.id,
        "cmd": type,
        "data": value
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def makeCommand(body){
	def options = [
     	"method": "POST",
        "path": "/tv/control",
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
