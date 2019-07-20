/**
 *  LG Dehumidifier(v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2019 fison67@nate.com
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
OPERATION_VALUE = [
    0: [val: "@AC_MAIN_OPERATION_MODE_COOL_W", str: ["EN": "Cool", "KR": "Cool"]],
	1: [val: "@AC_MAIN_OPERATION_MODE_DRY_W", str: ["EN": "Dry", "KR": "Dry"]],
    2: [val: "@AC_MAIN_OPERATION_MODE_FAN_W", str: ["EN": "Fan", "KR": "Fan"]],
    3: [val: "@AC_MAIN_OPERATION_MODE_AI_W", str: ["EN": "AI", "KR": "AI"]],
    4: [val: "@AC_MAIN_OPERATION_MODE_HEAT_W", str: ["EN": "Heat", "KR": "Heat"]],
    5: [val: "@AC_MAIN_OPERATION_MODE_AIRCLEAN_W", str: ["EN": "Air Clean", "KR": "공기청정"]],
    6: [val: "@AC_MAIN_OPERATION_MODE_ACO_W", str: ["EN": "Aco", "KR": "Aco"]],
    7: [val: "@AC_MAIN_OPERATION_MODE_AROMA_W", str: ["EN": "Aroma", "KR": "아로마"]],
    8: [val: "@AC_MAIN_OPERATION_MODE_ENERGY_SAVING_W", str: ["EN": "Energy Saving", "KR": "ㄸnergy saving"]],
    9: [val: "@AP_MAIN_MID_OPMODE_CLEAN_W", str: ["EN": "Clean", "KR": "청정"]],
    10: [val: "@AP_MAIN_MID_OPMODE_SLEEP_W", str: ["EN": "Sleep", "KR": "취침"]],
    11: [val: "@AP_MAIN_MID_OPMODE_SILENT_W", str: ["EN": "Silent", "KR": "정음청정"]],
    12: [val: "@AP_MAIN_MID_OPMODE_HUMIDITY_W", str: ["EN": "Humidity", "KR": "가습"]],
    13: [val: "@AP_MAIN_MID_OPMODE_CIRCULATOR_CLEAN_W", str: ["EN": "Circulator", "KR": "클린부스터"]],
    14: [val: "@AP_MAIN_MID_OPMODE_BABY_CARE_W", str: ["EN": "Baby Care", "KR": "싱글청정 중"]],
    15: [val: "@AP_MAIN_MID_OPMODE_DUAL_CLEAN_W", str: ["EN": "Dual Clean", "KR": "듀얼청정 중"]],
    16: [val: "@AP_MAIN_MID_OPMODE_AUTO_W", str: ["EN": "Auto", "KR": "자동"]],
    17: [val: "@AP_MAIN_MID_OPMODE_SMART_DEHUM_W", str: ["EN": "Smart Dehum", "KR": "스마트 제습"]],
    18: [val: "@AP_MAIN_MID_OPMODE_FAST_DEHUM_W", str: ["EN": "Fast Dehum", "KR": "쾌속 제습"]],
    19: [val: "@AP_MAIN_MID_OPMODE_CILENT_DEHUM_W", str: ["EN": "Cilent Dehum", "KR": "저소음 제습"]],
    20: [val: "@AP_MAIN_MID_OPMODE_CONCENTRATION_DRY_W", str: ["EN": "Clean Booster", "KR": "클린부스터"]],
    21: [val: "@AP_MAIN_MID_OPMODE_CLOTHING_DRY_W", str: ["EN": "Clothing Dry", "KR": "의류 건조"]],
    22: [val: "@AP_MAIN_MID_OPMODE_IONIZER_W", str: ["EN": "Ionzer", "KR": "집중 건조"]]
]

@Field 
WIND_VALUE = [
    0: [val:"@AP_MAIN_MID_WINDSTRENGTH_DHUM_LOWST_LOW_W", str: ["EN": "Lowst Low", "KR": "약약약"]],
    1: [val:"@AP_MAIN_MID_WINDSTRENGTH_DHUM_LOWST_W", str: ["EN": "Lowst", "KR": "약약"]],
    2: [val:"@AP_MAIN_MID_WINDSTRENGTH_DHUM_LOW_W", str: ["EN": "Low", "KR": "약"]],
    3: [val:"@AP_MAIN_MID_WINDSTRENGTH_DHUM_LOW_MID_W", str: ["EN": "Low Mid", "KR": "약중"]],
    4: [val:"@AP_MAIN_MID_WINDSTRENGTH_DHUM_MID_W", str: ["EN": "Mid", "KR": "중"]],
    5: [val:"@AP_MAIN_MID_WINDSTRENGTH_DHUM_MID_HIGH_W", str: ["EN": "Mid High", "KR": "중상"]],
    6: [val:"@AP_MAIN_MID_WINDSTRENGTH_DHUM_HIGH_W", str: ["EN": "High", "KR": "상"]],
    7: [val:"@AP_MAIN_MID_WINDSTRENGTH_DHUM_POWER_W", str: ["EN": "Power", "KR": "파워"]],
    8: [val:"@AP_MAIN_MID_WINDSTRENGTH_DHUM_AUTO_W", str: ["EN": "Auto", "KR": "오토"]]
]

metadata {
	definition (name: "LG Dehumidifier", namespace: "fison67", author: "fison67") {
        capability "Switch"
        capability "Switch Level"
        capability "Water Sensor"     //  ["dry", "wet"]
        capability "Relative Humidity Measurement"
        capability "Refresh"
        
        attribute "mode", "string"
        attribute "wind", "string"
        
        command "setMode", ["number"]
        command "mode1"
        command "mode2"
        command "mode3"
        command "mode4"
        command "mode5"
        command "mode6"
        
        command "setWind", ["number"]
        command "wind1"
        command "wind2"
	}

	simulator {
	}
    
	preferences {
        input name: "language", title:"Select a language" , type: "enum", required: true, options: ["EN", "KR"], defaultValue: "KR", description:"Language for DTH"
        
        input name: "mode1", title:"Mode#1 Type" , type: "number", required: false, defaultValue: 17
        input name: "mode2", title:"Mode#2 Type" , type: "number", required: false, defaultValue: 18
        input name: "mode3", title:"Mode#3 Type" , type: "number", required: false, defaultValue: 19
        input name: "mode4", title:"Mode#4 Type" , type: "number", required: false, defaultValue: 20
        input name: "mode5", title:"Mode#5 Type" , type: "number", required: false, defaultValue: 21
        input name: "mode6", title:"Mode#6 Type" , type: "number", required: false, defaultValue: 22
        
        input name: "wind1", title:"Wind#1 Type" , type: "number", required: false, defaultValue: 6
        input name: "wind2", title:"Wind#2 Type" , type: "number", required: false, defaultValue: 2
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
        controlTile("humidityControl", "device.level", "slider", range:"(30..70)", height: 1, width: 2) {
            state "level", action:"setLevel"
        }
        valueTile("humidity", "device.humidity", decoration: "flat", width: 1, height: 1) {
            state "default", label:'${currentValue}',  backgroundColors:[
                // Fahrenheit color set
                [value: 0, color: "#153591"],
                [value: 5, color: "#1e9cbb"],
                [value: 10, color: "#90d2a7"],
                [value: 15, color: "#44b621"],
                [value: 20, color: "#f1d801"],
                [value: 25, color: "#d04e00"],
                [value: 30, color: "#bc2323"],
                [value: 44, color: "#1e9cbb"],
                [value: 59, color: "#90d2a7"],
                [value: 74, color: "#44b621"],
                [value: 84, color: "#f1d801"],
                [value: 95, color: "#d04e00"],
                [value: 96, color: "#bc2323"]
            ]
        }
        valueTile("mode", "device.mode", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("mode1", "device.mode1", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "mode1"
		}
        valueTile("mode2", "device.mode2", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "mode2"
		}
        valueTile("mode3", "device.mode3", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "mode3"
		}
        valueTile("mode4", "device.mode4", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "mode4"
		}
        valueTile("mode5", "device.mode5", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "mode5"
		}
        valueTile("mode6", "device.mode6", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "mode6"
		}
        
        valueTile("wind1", "device.wind1", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "wind1"
		}
        valueTile("wind2", "device.wind2", decoration: "flat", height: 1, width: 1) {
			state "default", label:'${currentValue}', action: "wind2"
		}
        valueTile("waterStatus", "device.waterStatus", decoration: "flat", height: 1, width: 4) {
			state "default", label:'Water Tank: ${currentValue}'
		}
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def updated() {
	log.debug "updated"
	sendEvent(name: "mode1", value: OPERATION_VALUE[mode1]["str"][language])
	sendEvent(name: "mode2", value: OPERATION_VALUE[mode1]["str"][language])
	sendEvent(name: "mode3", value: OPERATION_VALUE[mode2]["str"][language])
	sendEvent(name: "mode4", value: OPERATION_VALUE[mode3]["str"][language])
	sendEvent(name: "mode5", value: OPERATION_VALUE[mode4]["str"][language])
    
	sendEvent(name: "wind1", value: WIND_VALUE[wind1]["str"][language])
	sendEvent(name: "wind2", value: WIND_VALUE[wind2]["str"][language])
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
    
    if(jsonObj.Operation){
    	sendEvent(name: "switch", value: (jsonObj.Operation.value == "0" ? "off" : "on"))
    }
    if(jsonObj.HumidityCfg){
    	sendEvent(name:"level", value: jsonObj.HumidityCfg.value as int)
    }
    if(jsonObj.SensorHumidity){
    	sendEvent(name: "humidity", value: jsonObj.SensorHumidity.value as int)
    }
    if(jsonObj.OpMode){
    	sendEvent(name: "mode", value: OPERATION_VALUE[jsonObj.OpMode.value as int]["str"][language])
    }
    if(jsonObj.WindStrength){
    	sendEvent(name: "wind", value: WIND_VALUE[jsonObj.WindStrength.value as int]["str"][language])
    }
    if(jsonObj.WatertankLight){
    	sendEvent(name: "water", value: (jsonObj.WatertankLight.value == "1" ?  "dry" : "wet"))
    	sendEvent(name: "waterStatus", value: (jsonObj.WatertankLight.value == "1" ?  "Not Full" : "Full"))
        
    }
    
    
    updateLastTime();
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now, displayed:false)
}

def setLevel(level){
	makeCommand("SetHumidityCfg", '{"HumidityCfg":"' + level + '"}')
}

def on(){
    makeCommand("SetOperationStart", "Start")
}

def off(){
	makeCommand("SetOperationStop", "Stop")
}

def setMode(val){
	makeCommand("SetOpMode", '{"OpMode":"' + val + '"}')
}

def mode1(){
    setMode(mode1)
}

def mode2(){
    setMode(mode2)
}

def mode3(){
    setMode(mode3)
}

def mode4(){
    setMode(mode4)
}

def mode5(){
    setMode(mode5)
}

def mode6(){
    setMode(mode6)
}

def setWind(val){
	makeCommand("SetWindStrength", '{"WindStrength":"' + val + '"}')
}

def wind1(){
    setWind(wind1)
}

def wind2(){
    setWind(wind2)
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
