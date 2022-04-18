import React from "react";

interface BackofficeNavigationBarButtonProps {
  title: string;
  icon: string;
  on: boolean;
  onClick: () => void;
}

export default class BackofficeNavigationBarButton extends React.Component<
  BackofficeNavigationBarButtonProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="navi-button-wrapper">
        <button
          className={`navi-bar-button ${this.props.on ? "navi-bar-on" : ""}`}
          onClick={() => this.props.onClick()}
        >
          <i className={`fas fa-${this.props.icon} fa-lg`}></i>
          <div className="pretendard navi-title">{this.props.title}</div>
        </button>
      </div>
    );
  }
}
