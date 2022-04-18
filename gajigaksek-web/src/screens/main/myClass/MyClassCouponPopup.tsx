import React from "react";
import CouponController from "../../../services/controllers/CouponController";
import { filterNumber } from "../../../utils/commonParams";

interface MyclassCouponPopupProps {
  on: boolean;
  lectureId: number;
  setVisible: (visible: boolean) => void;
}

interface MyclassCouponPopupStates {
  couponPrice: number;
  couponCount: number;
}

export default class MyclassCouponPopup extends React.Component<
  MyclassCouponPopupProps,
  MyclassCouponPopupStates
> {
  constructor(props: any) {
    super(props);
    this.state = { couponPrice: 0, couponCount: 0 };
  }

  private checkForm(params: any) {
    if (params.couponCount <= 0) {
      alert("쿠폰 갯수가 0개이하 입니다.");
      return false;
    }
    if (params.couponPrice <= 0) {
      alert("쿠폰 가격이 0원이하 입니다.");
      return false;
    }
    return true;
  }

  private async postCoupons(lectureId: number) {
    const params = {
      couponCount: this.state.couponCount,
      couponPrice: this.state.couponPrice,
    };

    if (!this.checkForm(params)) return;

    try {
      await CouponController.postCoupons(lectureId, params);
      this.props.setVisible(false);
    } catch (e) {}
  }

  render() {
    return (
      <div
        className={`home-popup-container ${
          this.props.on ? "home-popup-container-on" : ""
        }`}
      >
        <div className="class-add-schedule-popup">
          <div className="home-popup-title-container">
            <div className="home-popup-title pretendard">클래스 쿠폰 발급</div>
            <button
              className="home-popup-exit"
              onClick={() => this.props.setVisible(!this.props.on)}
            >
              <i className="fas fa-times fa-2x"></i>
            </button>
          </div>
          <div className="class-add-schedule-popup-main-container">
            <div className="class-add-price-coupon-coupon-wrapper">
              <div className="class-add-price-coupon-coupon-title pretendard">
                쿠폰 할인 금액
              </div>
              <div className="class-add-price-coupon-coupon-price-wrapper">
                <input
                  className="wtf-coupon pretendard"
                  type="text"
                  value={this.state.couponPrice}
                  onChange={(e: any) =>
                    this.setState({
                      couponPrice: filterNumber(e.target.value),
                    })
                  }
                ></input>
                <div className="pretendard">원</div>
              </div>
            </div>

            <div className="class-add-price-coupon-coupon-wrapper">
              <div className="class-add-price-coupon-coupon-title pretendard">
                쿠폰 발급 인원
              </div>
              <div className="class-add-price-coupon-coupon-input-wrapper">
                <button
                  onClick={() => {
                    if (this.state.couponCount > 0)
                      this.setState({
                        couponCount: this.state.couponCount - 1,
                      });
                  }}
                >
                  <i className="fas fa-minus fa-lg"></i>
                </button>
                <input
                  className="wtf-coupon pretendard"
                  value={this.state.couponCount}
                ></input>
                명
                <button
                  onClick={() =>
                    this.setState({ couponCount: this.state.couponCount + 1 })
                  }
                >
                  <i className="fas fa-plus fa-lg"></i>
                </button>
              </div>
              <div className="fucking-info pretendard">
                쿠폰은 발행일로 부터 30일간 사용 가능합니다.
              </div>
            </div>
            <button
              className="pretendard"
              onClick={() => this.postCoupons(this.props.lectureId)}
            >
              발급
            </button>
          </div>
        </div>
      </div>
    );
  }
}
