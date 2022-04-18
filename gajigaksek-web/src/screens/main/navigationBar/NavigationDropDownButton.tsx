import React from "react";
import "./NavigationDropDownButton.css";

interface NavigationDropDownButtonProps {
  title: string;
  on: boolean;
  onClick: () => void;
}

export default class NavigationDropDownButton extends React.Component<
  NavigationDropDownButtonProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="navi-dropdown-button-wrapper">
        <button
          className="navi-dropdown-button"
          onClick={() => this.props.onClick()}
        >
          <div className="pretendard navi-dropdown-title">
            {this.props.title}
          </div>
          <div
            className={this.props.on ? "navi-dropdown-on" : "navi-dropdown-off"}
          >
            <i className="fas fa-chevron-down fa"></i>
          </div>
        </button>
      </div>
    );
  }
}
