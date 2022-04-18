import React from "react";
import "./HomeNumberButton.css";

interface HomeNumberButtonProps {
  on: boolean;
  page: number;
  setPage: (page: number) => void;
}

export default class HomeNumberButton extends React.Component<
  HomeNumberButtonProps,
  Record<string, never>
> {
  render() {
    return (
      <button
        onClick={() => this.props.setPage(this.props.page)}
        className={`home-page-number${this.props.on ? "-on" : ""} pretendard`}
      >
        {this.props.page + 1}
      </button>
    );
  }
}
