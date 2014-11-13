// Generated code from Butter Knife. Do not modify!
package com.mcgrath.rockpaperscissors.main;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ResultFragment$$ViewInjector {
  public static void inject(Finder finder, final com.mcgrath.rockpaperscissors.main.ResultFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034186, "field 'mRecord'");
    target.mRecord = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131034182, "field 'mCpuAv'");
    target.mCpuAv = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131034185, "field 'mResult'");
    target.mResult = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131034177, "field 'mUserName'");
    target.mUserName = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131034181, "field 'mUserMove'");
    target.mUserMove = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131034187, "field 'mPlayAgainButton'");
    target.mPlayAgainButton = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131034176, "field 'mAvatarImage'");
    target.mAvatarImage = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131034184, "field 'mCpuMove'");
    target.mCpuMove = (android.widget.TextView) view;
  }

  public static void reset(com.mcgrath.rockpaperscissors.main.ResultFragment target) {
    target.mRecord = null;
    target.mCpuAv = null;
    target.mResult = null;
    target.mUserName = null;
    target.mUserMove = null;
    target.mPlayAgainButton = null;
    target.mAvatarImage = null;
    target.mCpuMove = null;
  }
}
