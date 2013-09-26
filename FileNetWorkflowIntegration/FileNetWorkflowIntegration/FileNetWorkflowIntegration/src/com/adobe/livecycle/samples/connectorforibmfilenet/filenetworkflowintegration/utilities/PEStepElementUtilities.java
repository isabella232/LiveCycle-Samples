/**
 * ADOBE SYSTEMS INCORPORATED
 * Copyright 2007 Adobe Systems Incorporated
 * All Rights Reserved
 * 
 * NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the
 * terms of the Adobe license agreement accompanying it.  If you have received this file from a
 * source other than Adobe, then your use, modification, or distribution of it requires the prior
 * written permission of Adobe.
 */
package com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities;

import filenet.vw.api.VWException;
import filenet.vw.api.VWQueue;
import filenet.vw.api.VWQueueElement;
import filenet.vw.api.VWQueueQuery;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWUserInfo;

public class PEStepElementUtilities
{

    public static void setStepParameterValues(VWStepElement aStepElement, java.util.HashMap aStepParamValueMap) 
    throws VWException
    {
        if (aStepParamValueMap != null && aStepParamValueMap.size() > 0)
        {
            for (java.util.Iterator _iter = aStepParamValueMap.entrySet().iterator(); _iter.hasNext(); )
            {
                java.util.Map.Entry _entry = (java.util.Map.Entry) _iter.next();
                String _param = (String) _entry.getKey();
                if (aStepElement.hasParameterName(_param))
                {
                    aStepElement.setParameterValue(_param, _entry.getValue(), true);    // A 'true' here will only set changed values 
                }
            }
        }
    }
        
    public static VWStepElement getStepElement(VWSession vwsession, String queueName, String wobNum)
    throws Exception
    {
        // TODO: Account for MS SQL, Oracle, DB2 seperately in queryFlags?  
        VWStepElement _vwStepElement = null;
        VWQueue _vwQueue = vwsession.getQueue(queueName);
        Object _minVal[] = { wobNum };
        
        // Check whether there is a step element 
        // Items that are MAX_VALUES_INCLUSIVE, MIN_VALUES_INCLUSIVE, QUERY_READ_BOUND [user or machine bound]
        byte _queryFlags0 = 98;
        VWQueueQuery _vwQueueQuery = _vwQueue.createQuery("F_WobNum", _minVal, _minVal, _queryFlags0, null, null, 5);
        if(_vwQueueQuery.hasNext())
        {
            _vwStepElement = (VWStepElement)_vwQueueQuery.next();
        } 
        else
        {
            // Check whether there is a queue element element 
            // Items that are MAX_VALUES_INCLUSIVE, MIN_VALUES_INCLUSIVE, QUERY_READ_BOUND [user or machine bound], locked/unlocked
            byte _queryFlags1 = 99;
            VWQueueQuery vwqueuequery1 = _vwQueue.createQuery("F_WobNum", _minVal, _minVal, _queryFlags1, null, null, 3);
            if(vwqueuequery1.hasNext())
            {
                String _userName = null;
                VWQueueElement _vwQueueElement = (VWQueueElement)vwqueuequery1.next();
                if(_vwQueueElement != null)
                {
                    String _lockedUser = _vwQueueElement.getLockedUser();
                    VWUserInfo _vwUserInfo = vwsession.fetchCurrentUserInfo();
                    if(_vwUserInfo != null)
                    {
                        _userName = _vwUserInfo.getName();
                    }

                    if(_lockedUser != null && _userName != null)
                    {
                        if (_lockedUser.compareToIgnoreCase(_userName) == 0)
                        {
                            byte queryFlags2 = 99;
                            VWQueueQuery _vwQueueQuery2 = 
                                _vwQueue.createQuery("F_WobNum", _minVal, _minVal, queryFlags2, null, null, 5);
                            if(_vwQueueQuery2.hasNext())
                            {
                                _vwStepElement = (VWStepElement)_vwQueueQuery2.next();
                            }
                        }
                    }
                }
            }
        }
        
        return _vwStepElement;
    }
}