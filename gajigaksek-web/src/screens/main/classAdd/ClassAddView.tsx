import React from "react";
import { Switch, Route } from "react-router-dom";
import CurriculumView from "./curriculum/CurriculumView";
import FirstView from "./first/FirstView";
import IntroView from "./intro/IntroView";
import PriceCouponView from "./priceCoupon/PriceCouponView";
import ScheduleView from "./schedule/ScheduleView";
import TermsView from "./terms/TermsView";

interface ClassAddViewProps {
  path: string;
}

export default class ClassAddView extends React.Component<
  ClassAddViewProps,
  Record<string, never>
> {
  render() {
    return (
      <Switch>
        <Route exact path={`${this.props.path}/first`}>
          <FirstView />
        </Route>
        <Route path={`${this.props.path}/intro`}>
          <IntroView />
        </Route>
        <Route path={`${this.props.path}/curriculum`}>
          <CurriculumView />
        </Route>
        <Route path={`${this.props.path}/schedule`}>
          <ScheduleView />
        </Route>
        <Route path={`${this.props.path}/price-coupon`}>
          <PriceCouponView />
        </Route>
        <Route path={`${this.props.path}/terms`}>
          <TermsView />
        </Route>
      </Switch>
    );
  }
}
