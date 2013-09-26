/*
 * ADOBE SYSTEMS INCORPORATED
 * Copyright 2007 Adobe Systems Incorporated All Rights Reserved
 *
 * NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the 
 * terms of the Adobe license agreement accompanying it.  If you have received this file from a 
 * source other than Adobe, then your use, modification, or distribution of it requires the prior 
 * written permission of Adobe.
 */

package com.adobe.livecycle.samples.connectorforemcdocumentum.renderandsubmitnrformusingwebtop;

import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfId;
import com.documentum.web.formext.config.IQualifier;
import com.documentum.web.formext.config.QualifierContext;
import com.documentum.web.formext.session.SessionManagerHttpBinding;

/**
 * Mimetype scope qualifier for Documentum wdk actions and components.
 * It allows "mimetype" of an object to be used as a qualifier for whether the
 * wdk action is applicable for that object or not.  
 * 
 * For more details on custom qualifiers, refer {@link http://www.dmdeveloper.com/articles/wdk/scoping.html}.
 * 
 * @author kmalik
 *
 */
public class MimeTypeQualifier implements IQualifier 
{

	private static final String MIME_TYPE="mimetype";
	private static final String OBJECT_ID="objectId";
	private static final String s_defaultMimeType="text/html";
	
	public MimeTypeQualifier()
	{}
	
	public String[] getAliasScopeValues(String s) 
	{
		return null;
	}

	public String getParentScopeValue(String s) 
	{
		return null;
	}

    /**
     * Returns the related context names used by this qualifier - "objectId" and "mimetype"
     */
	public String[] getContextNames() 
	{
		return new String[]{OBJECT_ID,MIME_TYPE};
	}

	/**
	 * Returns the official name of the scope the qualifier
     * resolves. i.e. "mimetype".
	 */
	public String getScopeName() 
	{
        return MIME_TYPE;
	}

	/**
	 * Returns the value of "mimetype" attribute for the given object.
	 * 
	 */
	public String getScopeValue(QualifierContext ctx) 
	{
		String strMimeType = null;
		try 
		{		
			strMimeType = ctx.get(MIME_TYPE);
			
			if(strMimeType==null || strMimeType.length()==0)
			{
				String strObjectID = ctx.get(OBJECT_ID);
				if(strObjectID!=null && strObjectID.length()!=0)
				{
					IDfSessionManager sMgr = SessionManagerHttpBinding.getSessionManager();
					String docbase = SessionManagerHttpBinding.getCurrentDocbase();
					IDfSession session = sMgr.getSession(docbase);			
					IDfPersistentObject persObj = session.getObject(new DfId(strObjectID));
					if(persObj instanceof IDfSysObject)
					{
						IDfSysObject sysObj = (IDfSysObject)persObj;
						strMimeType = sysObj.getFormat().getMIMEType();
					}
				}			
			}
		}catch (Exception e) 
		{}
		
		if(strMimeType==null || strMimeType.length()==0)
		{
			strMimeType = s_defaultMimeType;
		}
		
		return strMimeType;
	}

}
