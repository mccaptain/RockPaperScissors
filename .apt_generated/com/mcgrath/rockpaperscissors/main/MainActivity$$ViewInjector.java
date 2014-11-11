// Generated code from Butter Knife. Do not modify!
package com.mcgrath.rockpaperscissors.main;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewInjector {
  public static void inject(Finder finder, final com.mcgrath.rockpaperscissors.main.MainActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034155, "field 'mTitle'");
    target.mTitle = (android.widget.TextView) view;
  }

  public static void reset(com.mcgrath.rockpaperscissors.main.MainActivity target) {
    target.mTitle = null;
  }
}
