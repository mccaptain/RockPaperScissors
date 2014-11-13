// Generated code from Butter Knife. Do not modify!
package com.mcgrath.rockpaperscissors.main;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GameFragment$$ViewInjector {
  public static void inject(Finder finder, final com.mcgrath.rockpaperscissors.main.GameFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034177, "field 'mUserNameLabel'");
    target.mUserNameLabel = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131034180, "field 'mWheel'");
    target.mWheel = (com.anupcowkur.wheelmenu.WheelMenu) view;
    view = finder.findRequiredView(source, 2131034176, "field 'mAvatarImage'");
    target.mAvatarImage = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131034179, "field 'mPlayButton'");
    target.mPlayButton = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131034178, "field 'mUserEntryLabel'");
    target.mUserEntryLabel = (android.widget.TextView) view;
  }

  public static void reset(com.mcgrath.rockpaperscissors.main.GameFragment target) {
    target.mUserNameLabel = null;
    target.mWheel = null;
    target.mAvatarImage = null;
    target.mPlayButton = null;
    target.mUserEntryLabel = null;
  }
}
