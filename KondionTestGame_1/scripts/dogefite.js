World.passes.add(new GKO_DeferredPass(0));
/*World.passes[0].gui.add(new GUI_Button({
	x: 500,
	y: 500,
	width: 300,
	height: 300,
	text: "neat birds m8"//,
}))*/;

// Add light
SCN.LightB = new RKO_DirectionalLight();
SCN.LightB.color.set(1.0, 1.0, 1.0, 1.0);
// Add ground and wall
SCN.Ocean = new SKO_InfinitePlane();
SCN.Ocean.transform.rotateX(-Math.PI / 2);
SCN.Ocean.textureSize = 64;
SCN.Ocean.transform.translate(0, 0, -64);
SCN.Ocean.material = new Mat_Monotexture("water");//Mat_FlatColor(58 / 255, 189 / 255, 232 / 255);

//SCN.Wallz.transform.translate(0, 0, -40);
//SCN.Wallz.textureSize = 50;
//SCN.Wallz.transform.rotateX(-Math.PI / 2);

SCN.Platform = new SKO_Cube();
SCN.Platform.transform.translate(0, 10, 0);
SCN.Platform.transform.scale(3, 9, 3);
SCN.Platform.anchor = true;
SCN.Platform.setMaterial(new Mat_Monotexture("K_Cube"));
SCN.Platform.s = {
	onupdate: function() {
		if (KJS.i.keyboardDown(KJS.i.toGLFWCode('b')))
			this.obj.rotVelocity.x = 0.1;
	}
}

//SCN.PlatformB = new SKO_Cube();
//SCN.PlatformB.transform.translate(0, -10, 0);
//SCN.PlatformB.transform.scale(3, 3, 3);
//SCN.PlatformB.anchor = true;
//SCN.PlatformB.setMaterial(new Mat_Monotexture("K_Cube"));

KJS.d["------ Controls ------"] = 0;
KJS.d["WASD:"] = "Pitch, Roll";
KJS.d["QE"] = "Yaw";
KJS.d["SPACE"] = "Accelerate";
KJS.d["X"] = "Look behind";
KJS.d["MOUSE1"] = "Fire weapon";
KJS.d["V: Do not press"] = 0;
KJS.d["----------------------"] = 0;

SCN.planeA = flyingThing();
//SCN.planeB = flyingThing();
//SCN.FanBot = fanbot();
//SCN.FanBot.transform.m30 = 20;
SCN.planeA.s.onselect();
SCN.planeA.s.speed = 444;
SCN.planeA.s.on = true;
SCN.planeA.transform.m31 += 40;
SCN.planeA.transform.rotateY(Math.PI / 2);
SCN.s.players = [SCN.planeA];
//SCN.guy.s.on = true;
SCN.s.currentPlayer = 0;

var material = new Mat_Monotexture("K_Cube");

for (var i = 0; i < 100; i++) {
	var cube = new SKO_Cube();
	cube.transform.scale(30, 30, 30);
	//cube.morecube = new SKO_Cube(0);
	cube.transform.translate(KJS.orandom() * 500, 0, KJS.orandom() * 500);
	cube.setMaterial(material);
	cube.s = {
		scale: 30,
		delay: 6,
		boom: function() {
			if (this.delay == 0) {
				var f = this.scale;
				for (var j = 0; j < 8; j++) {
					var smol = new SKO_Cube();
					smol.setMaterial(material);
					smol.transform.scale(f / 2, f / 2, f / 2);
					smol.moveTo(this.obj.transform);
					smol.transform.translate(0, 0.1, 0);
					smol.velocity.x = KJS.orandom() * f * 3;
					smol.velocity.y = Math.random() * f * 3;
					smol.velocity.z = KJS.orandom() * f * 3;
					smol.s = {};
					smol.s.scale = f / 2;
					smol.s.delay = 6;
					smol.s.boom = this.boom;
					smol.s.onupdate = this.onupdate;
					SCN[SCN.nextName("CUBE_SMOL_")] = smol;
				}
				SCN.rescan();
				this.obj.killMe = true;
			}
		},
		onupdate: function() {
			this.delay = Math.max(0, this.delay - 1);
			this.obj.velocity.z -= Math.random() / 7;
			//this.obj.morecube.transform.translate(0, Math.sin(KJS.currentTick() / 20) / 10, 0);
			this.obj.rotVelocity.rotateY((Math.random() - 0.5) / 60);
		}
	}
	//cube.morecube.transform.translate(0, 1, 0);
	SCN["CUBE_" + i] = cube;
}

World.fogIntensity = 0.0001;
World.clearColor.set(0, 0, 0, 1);
World.skyColor.set(1, 1, 1, 1);
World.compMode = KJS.DEBUG;
World.s = {fpav: 0};

World.compositor = function(ctx, passes) {
	//print(ctx);
	//print(passes);
	ctx.fillRgb(1.0, 1.0, 1.0);
	passes[0].render();
	//passes[1].render();
	//passes[2].render();
	ctx.next();
	
	ctx.fillRgba(1.0, 1.0, 1.0, 1.0);
	var eggs = Math.max(Math.min(KJS.fps(), 1000), 0);
	//print("neat: " + eggs);
	//print("fuck: " + (this.s.fpav - eggs))
	this.s.fpav -= (this.s.fpav - eggs) / 20;//Math.floor(this.s.fpav - KJS.fps());
	//print("eggs: " + (this.s.fpav - Math.round(Math.floor(KJS.fps()) / 60)));
	ctx.drawImage(passes[0], 0, 0, passes[0].width, passes[0].height);
	//ctx.drawImage(passes[1], passes[0].width / 2, 0, passes[1].width / 6, passes[1].height / 6);
	//ctx.deferredRender(passes[0], passes[2], passes[1],
	//	0, 0, 0, 0, 0, 0,
	//	passes[0].width, passes[0].height);
	ctx.scale(0.5, 0.5);
	ctx.fillRgba(0.0, 0.0, 0.0, 0.5);
	ctx.fillRect(0, 0, 500, 68);
	ctx.textAlign = "left";
	ctx.fillRgba(1.0, 1.0, 1.0, 1.0);
	ctx.fillText("Frame r8: " + Math.floor(this.s.fpav) + " + Tap: " + (KJS.i.buttonTap(0)), 10, 30);
	ctx.fillText("Speed: " + (Math.floor(SCN.s.players[SCN.s.currentPlayer].s.speed * 3.6 * 100) / 100) + " km/h", 10, 60);

	passes[0].renderGUI(ctx);

	ctx.fillRgba(0.0, 1.0, 0.0, 1.0);
	ctx.identity();
	ctx.fillRect(KJS.i.getMouseX(), KJS.i.getMouseY(), 10, 10);
	
}
SCN.Camera = new OKO_Camera_();
SCN.Camera.moveTo(0, 4, 0);

SCN.s.onupdate = function() {
	//SCN.eggs.transform.rotateZ(0.01);
	//SCN.Wallz.transform.rotateX(Math.sin(KJS.currentTick() / 400) / 250);
	World.camera = this.players[this.currentPlayer].s.camera;
	
	if (KJS.i.mouseDown(1)) {
		KJS.i.setMouseLock(false);
	}
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('z')) && KJS.currentTick() % 60 == 3) {
		this.players[this.currentPlayer].s.on = false;
		this.currentPlayer = (this.currentPlayer + 1) % this.players.length;
		this.players[this.currentPlayer].s.on = true;
		this.players[this.currentPlayer].s.onselect();
	}
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('c')) && KJS.currentTick() % 60 == 3) {
		World.compMode ++;
		World.compMode %= 3;
	}
	if (KJS.i.keyboardDown(KJS.i.toGLFWCode('p'))) {
		KJS.s.clear();
		KJS.s.load("ktg1:scenes/menu");
	}
}

SCN.rescan();


var serverTest = function() {
	SCN.Server = new GKO_Server();
	SCN.Server.start(24558, 5);
	//SCN.Server.track(SCN.FanBot, 1);
}

var connectClientTest = function(host) {
	var client = new GKO_Client();
	client.s = {
		func: [
			function() { // make fan
				return null;
			},
			function() { // make fan
				var verybase = new SKO_Cube();
				var material = new Mat_Monotexture("fanbot");
				var camera = new OKO_Camera_(); // third person camera
				verybase.transform.rotateY(Math.PI / 2);
				verybase.hidden = true;
				verybase.base = new RKO_Obj(KJS.obj("fanbot_base"));
				verybase.base.setMaterial(material);
				verybase.base.transform.translate(0, -0.5, 0);
				verybase.base.head = new RKO_Obj(KJS.obj("fanbot_head"));
				verybase.base.head.setMaterial(material);
				verybase.base.head.transform.translate(0, 1.1, 0.25);
				verybase.base.head.fan = new RKO_Obj(KJS.obj("fanbot_fan"));
				verybase.base.head.fan.setMaterial(material);
				verybase.base.head.fan.transform.translate(0, 1.85961 - 1.1, 0.31 - 0.25);
				SCN[SCN.nextName("Fans")] = verybase;
				return verybase;
			}
		]
	}
	
	client.connect(host, 24558);
	SCN[SCN.nextName("Client")] = client;
}