import React from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import BackofficeNotificationView from "./notification/BackofficeNotificationView";
import BackofficeClassView from "./class/BackofficeClassView";
import BackofficeNavigationBar from "./navigationBar/BackofficeNavigationBar";
import BackofficeClassDetailView from "./class/BackofficeClassDetailView";
import BackofficeNoticeView from "./notice/BackofficeNoticeView";

interface BackofficeMainViewProps {
  path: string;
}

export default class BackofficeMainView extends React.Component<
  BackofficeMainViewProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="main-container">
        <BackofficeNavigationBar path={this.props.path} />
        <div className="main-separator"></div>

        <div className="main-main">
          <Switch>
            <Route exact path={`/${this.props.path}/notification`}>
              <BackofficeNotificationView />
            </Route>
            <Route path={`/${this.props.path}/class`}>
              <BackofficeClassView />
            </Route>
            <Route path={`/${this.props.path}/class-detail`}>
              <BackofficeClassDetailView
                path={`/${this.props.path}/class-detail`}
              />
            </Route>
            <Route path={`/${this.props.path}/notice`}>
              <BackofficeNoticeView />
            </Route>
          </Switch>
        </div>
      </div>
    );
  }
}
