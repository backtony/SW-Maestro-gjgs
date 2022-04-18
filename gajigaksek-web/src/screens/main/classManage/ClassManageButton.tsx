import React from "react";
import "./ClassManageButton.css";

interface ClassManageButtonProps {
  title: string;
  on: boolean;
  onClick: () => void;
}

export default class ClassManageButton extends React.Component<
  ClassManageButtonProps,
  Record<string, never>
> {
  render() {
    return (
      <button
        className={`class-manage-button pretendard ${
          this.props.on ? "class-manage-button-on" : ""
        }`}
        onClick={() => this.props.onClick()}
      >
        {this.props.title}
      </button>
    );
  }
}
