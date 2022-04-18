import React from "react";
import "./PriceBlock.css";

interface PriceBlockProps {
  price: number;
  subTitle: string;
  title: string;
  onChangeText: (text: string) => void;
}

export default class PriceBlock extends React.Component<
  PriceBlockProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="class-add-price-coupon-price-item">
        <div className="class-add-price-coupon-price-item-title-wrapper">
          <div className="class-add-price-coupon-price-item-subtitle pretendard">
            {this.props.subTitle}
          </div>
          <div className="class-add-price-coupon-price-item-title pretendard">
            {this.props.title}
          </div>
        </div>
        <div className="class-add-price-coupon-price-item-price-wrapper">
          <input
            type="text"
            value={this.props.price}
            onChange={(e: any) => this.props.onChangeText(e.target.value)}
            className="pretendard"
          ></input>
          <div className="pretendard">원</div>
        </div>
      </div>
    );
  }
}
