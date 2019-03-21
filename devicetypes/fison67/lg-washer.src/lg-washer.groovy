/**
 *  LG Warsher(v.0.0.3)
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
    5: [val: "@WM_STATE_INITIAL_W", str: ["EN":"INITIAL", "KR":"대기 중"] ],
    6: [val: "@WM_STATE_PAUSE_W", str: ["EN":"PAUSE", "KR":"일시정지 중"] ],
    7: [val: "@WM_STATE_ERROR_AUTO_OFF_W", str: ["EN":"ERROR", "KR":"에러 발생"] ],
    10: [val: "@WM_STATE_RESERVE_W", str: ["EN":"RESERVE", "KR":"예약 중"] ],
    20: [val: "@WM_STATE_DETECTING_W", str: ["EN":"DETECTING", "KR":"옷감량 확인 중"] ],
    21: [val: "WM_STATE_ADD_DRAIN_W", str: ["EN":"?", "KR":"?"] ],
    22: [val: "@WM_STATE_DETERGENT_AMOUNT_W", str: ["EN":"DETERGENT AMOUNT", "KR":"세제량 안내 중"] ],
    23: [val: "@WM_STATE_RUNNING_W", str: ["EN":"RUNNING", "KR":"세탁 중"] ],
    24: [val: "@WM_STATE_PREWASH_W", str: ["EN":"PREWASH", "KR":"애벌세탁 중"] ],
    30: [val: "@WM_STATE_RINSING_W", str: ["EN":"RINSING", "KR":"헹굼 중"] ],
    31: [val: "@WM_STATE_RINSE_HOLD_W", str: ["EN":"RINSE HOLD", "KR":"헹굼 대기 상태"] ],
    40: [val: "@WM_STATE_SPINNING_W", str: ["EN":"SPINNING", "KR":"탈수 중"] ],
    50: [val: "@WM_STATE_DRYING_W", str: ["EN":"DRYING", "KR":"건조 중"] ],
    60: [val: "@WM_STATE_END_W", str: ["EN":"END", "KR":"종료 상태"] ],
    61: [val: "@WM_STATE_FRESHCARE_W", str: ["EN":"FRESHCARE", "KR":"구김방지 중"] ],
    76: [val: "DEMO_MODE_NEWMOTION_PAUSE", str: ["EN":"?", "KR":"?"] ],
    77: [val: "DEMO_MODE_NEWMOTION_START", str: ["EN":"?", "KR":"?"] ],
    81: [val: "TCL_ALARM_NORMAL", str: ["EN":"?", "KR":"?"] ],
    83: [val: "@WM_STATE_FROZEN_PREVENT_INITIAL_W", str: ["EN":"FROZEN PREVENT INITIAL", "KR":"동결방지 모드 대기 중"] ],
    84: [val: "@WM_STATE_FROZEN_PREVENT_RUNNING_W", str: ["EN":"FROZEN PREVENT RUNNING", "KR":"동결방지 모드 실행 중"] ],
    85: [val: "@WM_STATE_FROZEN_PREVENT_PAUSE_W", str: ["EN":"FROZEN PREVENT PAUSE", "KR":"동결방지 모드 일시정지 중"] ],
    101: [val: "WM_STATE_AUDIBLE_DIAGNOSIS_W", str: ["EN":"?", "KR":"?"] ]
]

metadata {
	definition (name: "LG Washer", namespace: "fison67", author: "fison67") {
        capability "Sensor"
        capability "Switch Level"
        capability "Configuration"
        
        command "setStatus"
        
        attribute "leftMinute", "number"
        attribute "prvState", "string"
        attribute "curState", "string"
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
        
        valueTile("apCourse_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'AP Course'
        }
        valueTile("apCourse", "device.apCourse", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("leftTime_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Left Time'
        }
        valueTile("leftTime", "device.leftTime", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        
        
        valueTile("waterTemp_label", "", decoration: "flat", width: 2, height: 1) {
            state "default", label:'Water Temp'
        }
        valueTile("waterTemp", "device.waterTemp", decoration: "flat", width: 1, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("dryLevel_label", "", decoration: "flat", width: 2, height: 1) {
            state "default", label:'Dry Level'
        }
        valueTile("dryLevel", "device.dryLevel", decoration: "flat", width: 1, height: 1) {
            state "default", label:'${currentValue}'
        } 
        valueTile("spinSpeed_label", "", decoration: "flat", width: 2, height: 1) {
            state "default", label:'Spin Speed'
        }
        valueTile("spinSpeed", "device.spinSpeed", decoration: "flat", width: 1, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("soilLevel_label", "", decoration: "flat", width: 2, height: 1) {
            state "default", label:'Soil Level'
        }
        valueTile("soilLevel", "device.soilLevel", decoration: "flat", width: 1, height: 1) {
            state "default", label:'${currentValue}'
        }
        
        valueTile("tlcCount_label", "", decoration: "flat", width: 2, height: 1) {
            state "default", label:'Tub Clean Count'
        }
        valueTile("tlcCount", "device.tlcCount", decoration: "flat", width: 1, height: 1) {
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

/**
* {"Remain_Time_H":0,"Remain_Time_M":7,"Initial_Time_H":0,"Initial_Time_M":11,"APCourse":"스피드워시","Error":"No Error","SoilLevel":"선택 안함","WaterTemp":"선택 안함","DryLevel":"선택 안함","Reserve_Time_H":0,"Reserve_Time_M":0,"TCLCount":2,"LoadLevel":1}
* {"State":"전원 OFF","PreState":"대기 중"}}
* {"State":"대기 중","Remain_Time_H":0,"Remain_Time_M":0,"Initial_Time_H":0,"Initial_Time_M":0,"Error":"No Error","SoilLevel":"선택 안함","SpinSpeed":"선택 안함","WaterTemp":"선택 안함","RinseCount":"선택 안함","DryLevel":"선택 안함","Reserve_Time_H":0,"Reserve_Time_M":0,"PreState":"전원 OFF","TCLCount":2,"LoadLevel":0}
* {"State":28,"Remain_Time_H":0,"Remain_Time_M":5,"Initial_Time_H":0,"Initial_Time_M":27,"APCourse":"스피드워시","Error":"No Error","SoilLevel":"선택 안함","SpinSpeed":3,"WaterTemp":"선택 안함","RinseCount":"선택 안함","DryLevel":"선택 안함","Reserve_Time_H":0,"Reserve_Time_M":0,"Option1":0,"Option2":4,"Option3":0,"PreState":1,"SmartCourse":33,"TCLCount":30,"OPCourse":0,"LoadLevel":2}
* {"State":17,"Remain_Time_H":0,"Remain_Time_M":29,"Initial_Time_H":0,"Initial_Time_M":31,"APCourse":"스피드워시","Error":"No Error","SoilLevel":2,"SpinSpeed":3,"WaterTemp":3,"RinseCount":2,"DryLevel":"선택 안함","Reserve_Time_H":0,"Reserve_Time_M":0,"Option1":0,"Option2":4,"Option3":0,"PreState":14,"SmartCourse":33,"TCLCount":31,"OPCourse":0,"LoadLevel":3}
* {"State":17,"Remain_Time_H":0,"Remain_Time_M":20,"Initial_Time_H":0,"Initial_Time_M":31,"APCourse":"스피드워시","Error":"No Error","SoilLevel":2,"SpinSpeed":3,"WaterTemp":3,"RinseCount":2,"DryLevel":"선택 안함","Reserve_Time_H":0,"Reserve_Time_M":0,"Option1":0,"Option2":4,"Option3":0,"PreState":14,"SmartCourse":33,"TCLCount":31,"OPCourse":0,"LoadLevel":3}

*/
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
    
    if(jsonObj.APCourse != null){
    	sendEvent(name:"apCourse", value: jsonObj.APCourse.rValue)
	}
    
    if(jsonObj.Remain_Time_H != null){
    	state.remainTimeH = changeTime(jsonObj.Remain_Time_H.rValue)
	}
    if(jsonObj.Remain_Time_M != null){
    	state.remainTimeM = changeTime(jsonObj.Remain_Time_M.rValue)
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

def on(){
//	makeCommand("power", "on")
}

def off(){
//	makeCommand("power", "off")
}

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
