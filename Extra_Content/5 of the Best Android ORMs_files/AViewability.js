// v1.1.3.1
var that,
isMobile = false,
aniBox = document.querySelector('#aniBox'),
playerElement,
AVadConfig,
playerLoadedFirstTime = false,
playerInView = false,
startAdFirstTime = false,
aniBoxCSS = 'overflow:hidden; -webkit-transition:1s ease height; transition:1s ease height',
floatingSize = 0.5,
floatingRight = 0,
floatingbottom = 0,
floatingCSS,
counterError = 0,
customLogoInterval,
customLogo;

var AVAddClass = function (obj, css) {
	obj.style.cssText = css;
}
var AVSetCustomLogo = function () {
	customLogo = playerElement.querySelector('#aniview-credit');
	if (customLogo) {
		clearInterval(customLogoInterval);
		if (AVadConfig.customLogo.text || AVadConfig.customLogo.text == '') {
			customLogo.innerHTML = AVadConfig.customLogo.text + '<span>';
		}
		if (AVadConfig.customLogo.height) {
			customLogo.getElementsByTagName('SPAN')[0].style.height = AVadConfig.customLogo.height+ 'px';
		}
		if (AVadConfig.customLogo.width) {
			customLogo.getElementsByTagName('SPAN')[0].style.width = AVadConfig.customLogo.width  + 'px';
		}
		customLogo.getElementsByTagName('SPAN')[0].style.margin = '4 4 4 2';
		if (AVadConfig.customLogo.link) {
			customLogo.getElementsByTagName('SPAN')[0].style.backgroundImage = "url('" + AVadConfig.customLogo.link + "')";
		};
		if (AVadConfig.customLogo.clickthrough) {
			customLogo.onclick = function () {
				window.open(AVadConfig.customLogo.clickthrough);
			}
		}

	}else{
		customLogoInterval = setInterval(function(){ AVSetCustomLogo() }, 300);
	}
}
var AVSetfloating = function () {
	if (AVadConfig.floating.size) {
		floatingSize = AVadConfig.floating.size
	};
	if (AVadConfig.floating.right) {
		floatingRight = AVadConfig.floating.right
	};
	if (AVadConfig.floating.bottom) {
		floatingbottom = AVadConfig.floating.bottom
	};
	floatingCSS = 'z-index:10000001;position:fixed; bottom:' + floatingbottom + 'px; right:' + floatingRight + 'px; -webkit-transform:scale(' + floatingSize + '); -webkit-transform-origin:bottom right; transform:scale(' + floatingSize + '); transform-origin:bottom right'

}
var AVGetPlayerWidth = function (aniElement) {
	var avPlayerWidth = (aniElement.parentNode || aniElement.parentNode.tagName != 'BODY')
	 ? aniElement.parentNode.clientWidth
	 : document.body.clientWidth || screen.width;
	if (avPlayerWidth > 4000)
		avPlayerWidth = 4000;
	if (AVadConfig.width != 100 && AVadConfig.height != 0) {
		avPlayerWidth = AVadConfig.width;
	} else {
		AVadConfig.height = Math.floor(avPlayerWidth / 1.777);
		AVadConfig.width = avPlayerWidth;
	}
	return avPlayerWidth;
}

var AVHidePlayer = function () {
	aniBox.style.width = AVGetPlayerWidth(aniBox) + 'px';
	aniBox.style.height = '1px';
	playerElement.style.position = "absolute";
	playerElement.style.top = "-100000px";
}
var AVShowPlayer = function () {
	if (AVadConfig.customLogo) {
			AVSetCustomLogo();
	}
	aniBox.style.height = AVadConfig.height + 'px';
	that.resize(AVadConfig.width, AVadConfig.height);

	playerElement.style.top = "";
	playerElement.style.position = "";
	playerElement.style.zIndex = "9999999";

}
var AVStartAd = function () {
	if (playerInView && playerLoadedFirstTime && !startAdFirstTime) {
		startAdFirstTime = true;
		that.startAd();
	}
}
var AVStartViewability = function (myPlayer, avadconfig) {
	that = myPlayer;
	AVadConfig = avadconfig;

	if(AVadConfig.floting && !AVadConfig.floating){AVadConfig.floating = AVadConfig.floting};
	if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|ipad|iris|kindle|Android|Silk|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(navigator.userAgent)
		 || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(navigator.userAgent.substr(0, 4)))
		isMobile = true;
		
	if (!AVadConfig.preloader){
		AVadConfig.preloader = {};
	}
	
	playerElement = document.querySelector('#' + AVadConfig.position);

	AVAddClass(aniBox, aniBoxCSS);
	if (AVadConfig.floating){
		if (AVadConfig.floating.size>0){
			AVSetfloating();
		}
	}
	if (AVadConfig.playOnView && !AVadConfig.autoPlay){
		AVHidePlayer();
	}		
	
	myPlayer.onLoad = function () {
		
		playerLoadedFirstTime = true;
		AVStartAd();
	}
	myPlayer.onPlay = function () {
		if (AVadConfig.preloader.type!=''){
				AVShowPlayer();
		}else{
			AVShowPlayer();
		}
		
			

		if (AVadConfig.unMuteOnMouseEnter && !isMobile) {
			playerElement.addEventListener("mouseenter", function (a) {
				setTimeout(function () {
					that.unmute()
				}, 5)
			});
			playerElement.addEventListener("mouseleave", function (a) {
				setTimeout(function () {
					that.mute()
				}, 5)
			});
		}
		if (AVadConfig.pauseOnBlur && !isMobile) {
			window.addEventListener("blur", function () {
				setTimeout(function () {
					that.pause()
				},
					500)
			})
		}
	}
	myPlayer.onPlay100 = function () {
		if (!AVadConfig.lastFrame && (!AVadConfig.preloader.type)) {
				AVHidePlayer();
		}
	}
	myPlayer.onSkip = function () {
		AVHidePlayer();
	}
	myPlayer.onClose = function () {
		AVHidePlayer();
	}
	myPlayer.onError = function (a) {
		counterError = counterError+1;
		if (!AVadConfig.preloader.type && !AVadConfig.passbackUrl){
				AVHidePlayer();
		}else{
			if (AVadConfig.passbackUrl && AVadConfig.playOnView && counterError > AVadConfig.errorLimit){
				AVShowPlayer();
			}
		}
			
	}

	if (AVadConfig.playOnView) {
		myPlayer.startViewability("aniBox", function (isInView, ratio, state) {
			//console.log(isInView + " : " + ratio + " : " + state);
			if (isInView) {
				if (AVadConfig.preloader.type && !playerInView && AVadConfig.Preroll==0){

							AVShowPlayer();

				};
					
				playerInView = true;
				if(!that.playing){
					AVStartAd();
				};
				if (that.playing || (AVadConfig.preloader.type!='' && AVadConfig.preloader.type)) {
					if (AVadConfig.pauseOnUnseen)
						that.resume();
					AVAddClass(playerElement, '');

				} 
			} else {
				//Player is not In View but it playing
				if (that.playing || (AVadConfig.preloader.type!='' && AVadConfig.preloader.type)) {
					switch (state) {
					case 'OUT_TOP':
						if (AVadConfig.floating)
							AVAddClass(playerElement, floatingCSS);
						break;
					}

					if (AVadConfig.pauseOnUnseen)
						that.pause();
				}
			}
		}, 0.5);
	} else {
		startAdFirstTime = false;
		playerInView = true;
		playerLoadedFirstTime = true;
		AVStartAd();
		AVShowPlayer();
	}
};