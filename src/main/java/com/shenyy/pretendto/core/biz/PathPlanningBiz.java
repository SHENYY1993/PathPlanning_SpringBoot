package com.shenyy.pretendto.core.biz;

import com.alibaba.fastjson.JSONObject;

public interface PathPlanningBiz {
    JSONObject getPath(JSONObject scenario);

    JSONObject getPath2D(JSONObject scenario);

    JSONObject getPath3D(JSONObject scenario);
}
