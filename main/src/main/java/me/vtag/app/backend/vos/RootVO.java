package me.vtag.app.backend.vos;

import java.util.List;

import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.UserModel;

/**
 * Created by nageswara on 5/1/14.
 */
public class RootVO {
    public List<BaseTagModel> privatetags;
    public List<BaseTagModel> publictags;
    public List<BaseTagModel> followingtags;
    public TopTagsVO toptags;
    public UserModel user;
    public RootVO() {}
}
