import React from "react";
import "./ClassAddNaviButton.css";

interface ClassAddNaviButtonProps {
  on: boolean;
  title: string;
}

export default class ClassAddNaviButton extends React.Component<
  ClassAddNaviButtonProps,
  Record<string, never>
> {
  render() {
    return (
      <div
        className={`class-add-navi-button ${
          this.props.on ? "class-add-navi-button-on" : ""
        } pretendard`}
      >
        {this.props.title}
      </div>
    );
  }
}
