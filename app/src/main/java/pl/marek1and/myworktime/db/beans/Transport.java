package pl.marek1and.myworktime.db.beans;

import android.content.Context;

import pl.marek1and.myworktime.R;

public enum Transport {

    WALK(R.string.walk, "ic_walk"),
    BIKE(R.string.bike, "ic_bike"),
    CAR(R.string.car, "ic_car"),
    BUS(R.string.bus, "ic_bus"),
    CITY_BUS(R.string.city_bus, "ic_city_bus"),
    TRAMWAY(R.string.tramway, "ic_tramway"),
    TRAIN(R.string.train, "ic_train");

    private int nameRes;
    private String iconPath;

    private Transport(int nameRes, String iconPath) {
        this.nameRes = nameRes;
        this.iconPath = iconPath;
    }

    public long getId() {
        return ordinal();
    }

    private String getIconPath() {
        return String.format("%s:drawable/%s", "pl.marek1and.myworktime", iconPath);
    }

    public int getIconIdentifier(Context context) {
        return context.getResources().getIdentifier(getIconPath(), null, null);
    }

    public String getName(Context context) {
        return context.getString(nameRes);
    }
}
