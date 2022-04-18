import React from "react";
import "./NavigationBarButton.css";

interface NavigationBarButtonProps {
  title: string;
  on: boolean;
  onClick: () => void;
}

export default class NavigationBarButton extends React.Component<
  NavigationBarButtonProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="navi-button-wrapper">
        <button
          className={`navi-bar-button ${this.props.on ? "navi-bar-on" : ""}`}
          onClick={() => this.props.onClick()}
        >
          <i className="fas fa-home fa-lg"></i>
          <div className="pretendard navi-title">{this.props.title}</div>
        </button>
      </div>
    );
  }
}
