
+exit <- .send(controller_agent, achieve, updateRoomStatus).

+entrance <- .send(controller_agent, achieve, updateRoomStatus).

{ include("$jacamoJar/templates/common-cartago.asl") }
