
var a = {
		publisherId: "5961f0e6073ef42d7843d002",
		channelId: "59621939073ef4493f69bc25",
		width: 100,
		height: 0,
		errorLimit: 1,
		passbackUrl: "",
		lastFrame: true,
		HD: !0,
		autoPlay: false,
		Preroll: 0,
		midrolltime: 5,
		vastRetry: 1,
		loop: false,
		logo: true,
		timelineMode: "bottom",
		soundButton: true,
		pauseButton: true,
		preloader: {
			type: "",
			link: "",
			clickthrough: ""
		},
		customLogo: {
			link: "https://play.aniview.com/5858ed930b8ee8851941a7bb/59526d5028a06110545d0877/ads_by_hooly82x15.png",
			height: "14",
			width: "80",
			text: "     ",
			clickthrough: "http://hoolymedia.com"
		},
		playOnView: true,
		pauseOnBlur: true,
		unMuteOnMouseEnter: true,
		floating: {
			size: 0.66,
			right: 0,
			bottom: 80
		},
		pauseOnUnseen: false,
		closeButton: true,
		skipTimer: 7,
		skip: true,
		skipText: "Skip",
		position: "aniplayer"
};
(new Image).src="https://track1.aniview.com/track?pid="+a.publisherId+"&cid="+a.channelId+"&e=playerLoaded&cb="+Date.now();var AV_getParams=function(b){AV_scripts=document.getElementsByTagName("script");for(var d="",c=0;c<AV_scripts.length;c++)if(-1<AV_scripts[c].id.indexOf(b))if(d=AV_scripts[c].src.split("?").pop())break;else return"";return d},AV_ref1=AV_getParams("aniviewJS");a.ref1=AV_ref1;var AV_aniDIV,AV_scripts,AV_body,AV_aniviewJS,AV_insertElement,AV_scriptTag;AV_aniDIV='<div style="width:100%;margin: 0 auto; max-width: 640px;" ><div id="aniBox" style="height:1px;"><div id="aniplayer"></div></div></div>';document.body?(AV_aniviewJS=document.querySelector("#aniviewJS"),AV_insertElement=document.createElement("div"),AV_insertElement.innerHTML=AV_aniDIV,AV_aniviewJS.parentNode.insertBefore(AV_insertElement,AV_aniviewJS)):(AV_body=document.createElement("body"),AV_body.innerHTML=AV_aniDIV,document.documentElement.appendChild(AV_body));var AV_getTopElement=function(b){try{var d;var c=window.top.document.getElementsByTagName("iframe");var e=window;for(b=0;b<c.length;b++)if(c[b].contentWindow==e)return AV_targetFrame=d=c[b],supportViewability=!0,window.top.document;supportViewability=!0;return document}catch(f){return supportViewability=!1,document}},AV_setNewPosition=function(b,d){if(""!=b||""!=d){if(""!=b){var c=b.split("#");var e=AV_topElement.getElementsByTagName(c[0]);c=e[c[1]-1]}else c=AV_topElement.getElementById(d);e=AV_targetFrame?AV_targetFrame.contentDocument||AV_targetFrame.contentWindow?AV_targetFrame.contentDocument.getElementsByTagName("body")[0]||AV_targetFrame.contentWindow.document.getElementsByTagName("body")[0]:AV_topElement.getElementById("aniBox").parentNode.parentNode:AV_topElement.getElementById("aniBox").parentNode.parentNode;c.innerHTML+=e.innerHTML}},AV_loadJS=function(b,d,c){AV_scriptTag=AV_topElement.createElement("script");AV_scriptTag.src=b;AV_scriptTag.onload=d;AV_scriptTag.onreadystatechange=d;c.appendChild(AV_scriptTag)},AV_loadAniviewJS=function(){AV_loadJS("https://player.aniview.com/script/6.1/aniview.js",AV_StartPlayer,AV_topElement.body)},AV_StartPlayer=function(){if("function"==typeof parent.avPlayer){var b=new parent.avPlayer(a);parent.AVStartViewability(b,a)}else b=new avPlayer(a),AVStartViewability(b,a);b.play(a)},AV_targetFrame,AV_PositionByID="",AV_PositionByTagName="";""!=AV_PositionByTagName||""!=AV_PositionByID?(AV_topElement=AV_getTopElement("aniBox"),AV_setNewPosition(AV_PositionByTagName,AV_PositionByID)):AV_topElement=document;AV_loadJS("https://play.aniview.com/Templates/AViewability.js",AV_loadAniviewJS,AV_topElement.body);