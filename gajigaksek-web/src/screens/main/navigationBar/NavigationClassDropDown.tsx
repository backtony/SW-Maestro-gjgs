import React from "react";
import NavigationDropDownButton from "./NavigationDropDownButton";
import NavigationInnerButton from "./NavigationInnerButton";

interface NavigationClassDropDownProps {
  path: string;
  dropdown: boolean;
  setDropDown: (dropdown: boolean) => void;
}

export default class NavigationClassDropDown extends React.Component<
  NavigationClassDropDownProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="navi-dropdown">
        <NavigationDropDownButton
          title={"클래스 관리"}
          onClick={() => this.props.setDropDown(!this.props.dropdown)}
          on={this.props.dropdown}
        />
        <div
          className={
            this.props.dropdown ? "navi-button-list-on" : "navi-button-list"
          }
        >
          <NavigationInnerButton
            title={"내 클래스"}
            icon={"graduation-cap"}
            onClick={() => {
              window.location.href = `/${this.props.path}/myclass`;
            }}
            on={window.location.href.indexOf("/myclass") > -1}
          />
          <NavigationInnerButton
            title={"클래스 관리"}
            icon={"user-cog"}
            onClick={() => {
              window.location.href = `/${this.props.path}/class-manage`;
            }}
            on={window.location.href.indexOf("/class-manage") > -1}
          />
          <NavigationInnerButton
            title={"문의 관리"}
            icon={"question-circle"}
            onClick={() => {
              window.location.href = `/${this.props.path}/question-manage`;
            }}
            on={window.location.href.indexOf("/question-manage") > -1}
          />
          <NavigationInnerButton
            title={"리뷰 관리"}
            icon={"comment-alt"}
            onClick={() => {
              window.location.href = `/${this.props.path}/review-manage`;
            }}
            on={window.location.href.indexOf("/review-manage") > -1}
          />
          <NavigationInnerButton
            title={"채팅"}
            icon={"comments"}
            onClick={() => {
              window.location.href = `/${this.props.path}/chat`;
            }}
            on={window.location.href.indexOf("/chat") > -1}
          />
          <NavigationInnerButton
            title={"쿠폰 관리"}
            icon={"gift"}
            onClick={() => {
              window.location.href = `/${this.props.path}/coupon-manage`;
            }}
            on={window.location.href.indexOf("/coupon-manage") > -1}
          />
        </div>
      </div>
    );
  }
}
