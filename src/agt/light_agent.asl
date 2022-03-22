
+!switchOffAction : state(State) & State == "ON" <-
    .println("Switching lamp off");
    switchOff.

+!switchOffAction.

+!increaseBrightnessAction : lightBrightnessLevel(Level) <-
    .println("Increasing lamp brightness to ", Level + 1);
    increaseBrightness.


+!switchOnAndIncreaseBrightness : state(State) & State == "OFF" <-
    .println("Switching lamp on");
    switchOn;
    !increaseBrightnessAction.

+!switchOnAndIncreaseBrightness : state(State) & State == "ON" <-
    !increaseBrightnessAction.

{ include("$jacamoJar/templates/common-cartago.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
