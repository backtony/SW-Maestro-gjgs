import React from "react";
import "./ClassAddGeneralButton.css";

interface ClassAddGeneralButtonProps {
  onClick: () => void;
  title: string;
  on: boolean;
}

export default class ClassAddGeneralButton extends React.Component<
  ClassAddGeneralButtonProps,
  Record<string, never>
> {
  render() {
    return (
      <button
        className={`class-add-general-button ${
          this.props.on ? "class-add-general-button-on" : ""
        } pretendard`}
        onClick={() => this.props.onClick()}
      >
        {this.props.title}
      </button>
    );
  }
}
