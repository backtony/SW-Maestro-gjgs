import React from "react";
import "./NavigationInnerButton.css";

interface NavigationInnerButtonProps {
  title: string;
  icon: string;
  on: boolean;
  onClick: () => void;
}

export default class NavigationInnerButton extends React.Component<
  NavigationInnerButtonProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="navi-inner-button-wrapper">
        <button
          className={`navi-inner-button ${
            this.props.on ? "navi-inner-on" : ""
          }`}
          onClick={() => this.props.onClick()}
        >
          <i className={`fas fa-${this.props.icon} fa-lg`}></i>
          <div className="pretendard navi-inner-title">{this.props.title}</div>
        </button>
      </div>
    );
  }
}
