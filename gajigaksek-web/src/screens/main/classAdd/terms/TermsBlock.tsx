import React from "react";
import "./TermsBlock.css";

interface TermsBlockProps {
  title: string;
  text: string;
  checked: boolean;
  onClick: (checked: boolean) => void;
}

export default class TermsBlock extends React.Component<
  TermsBlockProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="class-add-first-name-container">
        <div className="class-add-terms-title-wrapper">
          <input
            type="checkbox"
            className="class-add-terms-checkbox"
            checked={this.props.checked}
            onClick={() => this.props.onClick(!this.props.checked)}
          ></input>

          <div className="class-add-mainCategory-title pretendard">
            {this.props.title}
          </div>
        </div>
        <div className="class-add-terms-text-wrapper">
          <textarea className="pretendard">{this.props.text}</textarea>
        </div>
      </div>
    );
  }
}
