import React from "react";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import ChatView from "./chat/ChatView";
import ClassAddView from "./classAdd/ClassAddView";
import ClassManageView from "./classManage/ClassManageView";
import CouponManageView from "./couponManage/CouponManageView";
import HomeBulletinDetail from "./home/bulletin/HomeBulletinDetail";
import HomeView from "./home/HomeView";
import "./MainView.css";
import MyClassScheduleView from "./myClass/MyClassSchduleView";
import MyClassView from "./myClass/MyClassView";
import NavigationBar from "./navigationBar/NavigationBar";
import QuestionManageView from "./questionManage/QuestionManageView";
import ReviewManageView from "./reviewManage/ReviewManageView";

interface HomeProps {
  path: string;
}

export default class MainView extends React.Component<
  HomeProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="main-container">
        <NavigationBar path={this.props.path} />
        <div className="main-separator"></div>

        <div className="main-main">
          <Switch>
            <Route exact path={`/${this.props.path}/home`}>
              <HomeView />
            </Route>
            <Route path={`/${this.props.path}/add/class`}>
              <ClassAddView path={`/${this.props.path}/add/class`} />
            </Route>
            <Route path={`/${this.props.path}/myclass`}>
              <MyClassView path={`/${this.props.path}/myclass`} />
            </Route>
            <Route path={`/${this.props.path}/edit-schedule`}>
              <MyClassScheduleView />
            </Route>
            <Route path={`/${this.props.path}/class-manage`}>
              <ClassManageView path={`/${this.props.path}/class-manage`} />
            </Route>
            <Route path={`/${this.props.path}/question-manage`}>
              <QuestionManageView
                path={`/${this.props.path}/question-manage`}
              />
            </Route>
            <Route path={`/${this.props.path}/review-manage`}>
              <ReviewManageView path={`/${this.props.path}/review-manage`} />
            </Route>
            <Route path={`/${this.props.path}/coupon-manage`}>
              <CouponManageView path={`/${this.props.path}/coupon-manage`} />
            </Route>
            <Route path={`/${this.props.path}/chat`}>
              <ChatView />
            </Route>
            <Route path={`/${this.props.path}/bulletin`}>
              <HomeBulletinDetail />
            </Route>
          </Switch>
        </div>
      </div>
    );
  }
}

function About() {
  return <h2>About</h2>;
}

function Users() {
  return <h2>Users</h2>;
}
