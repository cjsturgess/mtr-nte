package cn.zbx1425.mtrsteamloco.mixin;

import mtr.data.TrainClient;
import mtr.data.VehicleRidingClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TrainClient.class)
public interface TrainClientAccessor {

    @Accessor(remap = false)
    VehicleRidingClient getVehicleRidingClient();
}