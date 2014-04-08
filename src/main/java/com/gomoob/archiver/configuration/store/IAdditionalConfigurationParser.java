package com.gomoob.archiver.configuration.store;

import org.json.JSONObject;

public interface IAdditionalConfigurationParser {

    IAdditionalConfiguration parse(JSONObject jsonObject);

}
