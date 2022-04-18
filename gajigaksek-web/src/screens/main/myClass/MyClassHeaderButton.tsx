import React from "react";
import "./MyClassHeaderButton.css";

interface MyClassHeaderButtonProps {
  title: string;
  on: boolean;
  onClick: () => void;
}

export default class MyClassHeaderButton extends React.Component<
  MyClassHeaderButtonProps,
  Record<string, never>
> {
  render() {
    return (
      <button
        className="myclass-header-button"
        onClick={() => this.props.onClick()}
      >
        <div
          className={`myclass-header-button-title pretendard ${
            this.props.on ? "myclass-header-button-on-color" : ""
          }`}
        >
          {this.props.title}
        </div>
        <div
          className={`myclass-header-button-indicator ${
            this.props.on ? "myclass-header-button-on-background" : ""
          }`}
        />
      </button>
    );
  }
}
