// Generated code from Butter Knife. Do not modify!
package com.example.butterknifedemo;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewInjector {
  public static void inject(Finder finder, final com.example.butterknifedemo.MainActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131230721);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131230721' for field 'tvLabel' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.tvLabel = (android.widget.TextView) view;
    view = finder.findById(source, 2131230720);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131230720' for field 'btnPerform' and method 'onLabelClick' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.btnPerform = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onLabelClick((android.widget.Button) p0);
        }
      });
  }

  public static void reset(com.example.butterknifedemo.MainActivity target) {
    target.tvLabel = null;
    target.btnPerform = null;
  }
}
