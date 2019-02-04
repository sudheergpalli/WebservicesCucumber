package com.adp.autopay.automation.commonlibrary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gadiv on 2/20/2016.
 */

public class DataObject {

    private String baseUrl;
    private String serviceName;
    private String regionId;
    private String userStory;
    private String testDescription;
    private String orgId;
    private String companyCode;
    private String httpMethod;
    private String headerParams;
    private String environment;
    private String serverName;
    private String itemId;
    private String ADP_UserID;
    private String sm_serversessionid;
    private String associateOID;
    private String ORGOID;
    private int rowNum;
    private List<String> headers;
    private List<String> errorCodes;
    private List<String> errorDescriptions;
    private List<String> devErrorCodes;
    private List<String> devErrorDescriptions;
    private Map<String, String> urlParams = new HashMap<String, String>();

    
    public List<String> getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(List<String> errorCodes) {
		this.errorCodes = errorCodes;
	}

	public List<String> getErrorDescriptions() {
		return errorDescriptions;
	}

	public void setErrorDescriptions(List<String> errorDescriptions) {
		this.errorDescriptions = errorDescriptions;
	}

	 public List<String> getdevErrorCodes() {
			return devErrorCodes;
		}

		public void setdevErrorCodes(List<String> devErrorCodes) {
			this.devErrorCodes = devErrorCodes;
		}

		public List<String> getdevErrorDescriptions() {
			return devErrorDescriptions;
		}

		public void setdevErrorDescriptions(List<String> devErrorDescriptions) {
			this.devErrorDescriptions = devErrorDescriptions;
		}
	
	
	public Map<String, String> getUrlParams() {
		return urlParams;
	}

	public void setUrlParams(Map<String, String> urlParams) {
		this.urlParams = urlParams;
	}

	public String getTestDescription() {
		return testDescription;
	}

	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public String getSm_serversessionid() {
        return sm_serversessionid;
    }

    public void setSm_serversessionid(String sm_serversessionid) {
        this.sm_serversessionid = sm_serversessionid;
    }

    public String getADP_UserID() {
        return ADP_UserID;
    }

    public void setADP_UserID(String ADP_UserID) {
        this.ADP_UserID = ADP_UserID;
    }

    public String getAssociateOID() {
        return associateOID;
    }

    public void setAssociateOID(String associateOID) {
        this.associateOID = associateOID;
    }

    public String getORGOID() {
        return ORGOID;
    }

    public void setORGOID(String ORGOID) {
        this.ORGOID = ORGOID;
    }


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getUserStory() {
        return userStory;
    }

    public void setUserStory(String userStory) {
        this.userStory = userStory;
    }

    public String gettestDescription() {
        return testDescription;
    }

    public void settestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHeaderParams() {
        return headerParams;
    }

    public void setHeaderParams(String headerParams) {
        this.headerParams = headerParams;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataObject that = (DataObject) o;

        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null) return false;
        return environment != null ? environment.equals(that.environment) : that.environment == null;

    }

    @Override
    public int hashCode() {
        int result = serviceName != null ? serviceName.hashCode() : 0;
        result = 31 * result + (environment != null ? environment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataObject{" +
                "baseUrl='" + baseUrl + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", regionId='" + regionId + '\'' +
                ", userStory='" + userStory + '\'' +
                ", testDescription='" + testDescription + '\'' +
                ", orgId='" + orgId + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", headerParams='" + headerParams + '\'' +
                ", environment='" + environment + '\'' +
                ", serverName='" + serverName + '\'' +
                ", itemID='" + itemId + '\'' +
                ", ADP-UserID='" + ADP_UserID + '\'' +
                ", sm_serversessionid='" + sm_serversessionid + '\'' +
                ", associateOID='" + associateOID + '\'' +
                ", ORGOID='" + ORGOID + '\'' +
                '}';
    }
}

