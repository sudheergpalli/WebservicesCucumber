package com.adp.autopay.automation.commonlibrary;

import com.adp.autopay.automation.utility.PropertiesFile;

/**
 * Created by gadiv on 2/20/2016.
 */
public class CommonUtility {

    public static String buildURL(DataObject object) {
        String url = PropertiesFile.GetEnvironmentProperty(object.getEnvironment()) + object.getBaseUrl();
//       url = url.replaceAll("[{}]", "");
        for (String urlParam : object.getUrlParams().keySet()) {

            if (object.getUrlParams().get(urlParam).equalsIgnoreCase("*BLANK*")){
                url = url.replace("{" + urlParam + "}", "");
            }
            else
            {
                url = url.replace("{" + urlParam + "}", object.getUrlParams().get(urlParam));
            }

            //url = url.replace("{" + urlParam + "}", object.getUrlParams().get(urlParam));
		}
       /*url = url.replace("{orgoid}", object.getOrgId());
       url = url.replace("{region}", object.getRegionId());
       url = url.replace("{company}", object.getCompanyCode());
        if(object.getItemId() != null) {
           url = url.replace("{ITEMID}", object.getItemId());
        }*/
        return url;
    }

}
