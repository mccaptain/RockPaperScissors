// Generated code from Butter Knife. Do not modify!
package com.mcgrath.rockpaperscissors.main;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class UserInputFragment$$ViewInjector {
  public static void inject(Finder finder, final com.mcgrath.rockpaperscissors.main.UserInputFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034192, "field 'mUserNameEditbox'");
    target.mUserNameEditbox = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131034195, "field 'mInputButton'");
    target.mInputButton = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131034196, "field 'mUserSpinner'");
    target.mUserSpinner = (android.widget.Spinner) view;
    view = finder.findRequiredView(source, 2131034197, "field 'mLoadButton'");
    target.mLoadButton = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131034193, "field 'mUserAgePicker'");
    target.mUserAgePicker = (android.widget.NumberPicker) view;
    view = finder.findRequiredView(source, 2131034194, "field 'mFemaleSexCheckBox'");
    target.mFemaleSexCheckBox = (android.widget.CheckBox) view;
  }

  public static void reset(com.mcgrath.rockpaperscissors.main.UserInputFragment target) {
    target.mUserNameEditbox = null;
    target.mInputButton = null;
    target.mUserSpinner = null;
    target.mLoadButton = null;
    target.mUserAgePicker = null;
    target.mFemaleSexCheckBox = null;
  }
}
