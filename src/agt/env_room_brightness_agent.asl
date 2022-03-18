
+externalBrightnessLevel(Level): lightBrightnessLevel(Value) <-
    setBrightnessLevel(Value + Level).

+lightBrightnessLevel(Level):  externalBrightnessLevel(Value)<-
    setBrightnessLevel(Level + Value).

{ include("$jacamoJar/templates/common-cartago.asl") }