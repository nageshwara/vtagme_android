package me.vtag.app.backend.vos;

import java.util.List;

import me.vtag.app.backend.models.BaseTagModel;
import me.vtag.app.backend.models.HashtagModel;
import me.vtag.app.backend.models.PrivatetagModel;
import me.vtag.app.backend.models.PublictagModel;
import me.vtag.app.backend.models.UserModel;

/**
 * Created by nageswara on 5/1/14.
 */
public class RootVO {
    public List<PrivatetagModel> privatetags;
    public List<PublictagModel> publictags;
    public List<HashtagModel> followingtags;
    public TopTagsVO toptags;
    public UserModel user;
    public RootVO() {}
}
