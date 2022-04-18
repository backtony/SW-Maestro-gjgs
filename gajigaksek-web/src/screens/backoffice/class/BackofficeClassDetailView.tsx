import React from "react";
import { Switch, Route } from "react-router-dom";
import BackofficeCurriculumView from "./detail/curriculum/BackofficeCurriculumView";
import BackofficeFirstView from "./detail/first/BackofficeFirstView";
import BackofficeIntroView from "./detail/intro/BackofficeIntroView";
import BackofficePriceCouponView from "./detail/priceCoupon/BackofficePriceCouponView";
import BackofficeScheduleView from "./detail/schedule/BackofficeScheduleView";

interface BackofficeClassDetailViewProps {
  path: string;
}

export default class BackofficeClassDetailView extends React.Component<
  BackofficeClassDetailViewProps,
  Record<string, never>
> {
  render() {
    return (
      <Switch>
        <Route exact path={`${this.props.path}/first`}>
          <BackofficeFirstView />
        </Route>
        <Route path={`${this.props.path}/intro`}>
          <BackofficeIntroView />
        </Route>
        <Route path={`${this.props.path}/curriculum`}>
          <BackofficeCurriculumView />
        </Route>
        <Route path={`${this.props.path}/schedule`}>
          <BackofficeScheduleView />
        </Route>
        <Route path={`${this.props.path}/price-coupon`}>
          <BackofficePriceCouponView />
        </Route>
      </Switch>
    );
  }
}
