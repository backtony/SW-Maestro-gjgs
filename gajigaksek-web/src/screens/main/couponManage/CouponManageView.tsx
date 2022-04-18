import React from "react";
import CouponController from "../../../services/controllers/CouponController";
import CouponManageItem from "./CouponManageItem";
import "./CouponManageView.css";

export class CouponInfo {
  constructor() {
    this.title =
      this.type =
      this.issueDate =
      this.closeDate =
      this.discountPrice =
        "";
    this.lectureId =
      this.chargeCount =
      this.receivePeople =
      this.remainCount =
        0;
    this.checked = false;
  }
  lectureId: number;
  title: string;
  type: string;
  issueDate: string;
  closeDate: string;
  discountPrice: string;
  chargeCount: number;
  receivePeople: number;
  remainCount: number;
  checked: boolean;
}

interface CouponManageViewProps {
  path: string;
}

interface CouponManageViewStates {
  coupons: CouponInfo[];
}

const couponList = [
  {
    lectureId: 0,
    title: "마카롱 만들기",
    type: "쿠폰발급형태",
    issueDate: "2020.08.01",
    closeDate: "2020.08.29",
    discountPrice: "50,000원",
    chargeCount: 99,
    receivePeople: 45,
    remainCount: 54,
    checked: false,
  },
  {
    lectureId: 0,
    title: "마카롱 만들기",
    type: "쿠폰발급형태",
    issueDate: "2020.08.01",
    closeDate: "2020.08.29",
    discountPrice: "50,000원",
    chargeCount: 99,
    receivePeople: 45,
    remainCount: 54,
    checked: false,
  },
  {
    lectureId: 0,
    title: "마카롱 만들기",
    type: "쿠폰발급형태",
    issueDate: "2020.08.01",
    closeDate: "2020.08.29",
    discountPrice: "50,000원",
    chargeCount: 99,
    receivePeople: 45,
    remainCount: 54,
    checked: false,
  },
];

export default class CouponManageView extends React.Component<
  CouponManageViewProps,
  CouponManageViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = { coupons: couponList };
    this.getCoupons();
  }

  private async getCoupons() {
    try {
      const res = await CouponController.getCoupons();
      const data = res.data;
      this.setState({ coupons: data.couponResponseList });
    } catch (e) {}
  }

  private async patchCoupons(lectureId: number) {
    try {
      await CouponController.patchCoupons(lectureId);
      alert("선택한 쿠폰이 마감처리되었습니다.");
      window.location.href = "/main/coupon-manage";
    } catch (e) {}
  }

  render() {
    return (
      <div className="myclass-container">
        <div className="myclass-header pretendard">할인쿠폰 관리</div>
        <div className="coupon-manage-detail-text pretendard">
          직접 발행한 할인쿠폰 발행현황을 확인 하실 수 있으며, 발행 쿠폰 중단
          또는 재발행 설정이 가능합니다. 할인쿠폰 발행은 내 클래스 메뉴에서 직접
          발급 가능합니다.
        </div>
        <div className="coupon-manage-button-wrapper">
          <button
            className="coupon-manage-button t2"
            onClick={() =>
              this.state.coupons
                .filter((coupon: CouponInfo) => coupon.checked)
                .forEach((coupon: CouponInfo) =>
                  this.patchCoupons(coupon.lectureId)
                )
            }
          >
            <i className="fas fa-times fa-lg"></i>
            <div className="pretendard">선택한 쿠폰 마감</div>
          </button>
          {/* <button className="coupon-manage-button t3">
            <i className="fas fa-times fa-lg"></i>
            <div className="pretendard">선택한 쿠폰 재발급</div>
          </button> */}
        </div>
        <div className="coupon-manage-header">
          <div className="coupon-manage-header1 pretendard">
            <div>
              <i className="far fa-square fa-lg"></i>
            </div>
          </div>
          <div className="coupon-manage-header2 pretendard">
            <div>클래스명</div>
          </div>
          <div className="coupon-manage-header3 pretendard">
            <div>활성화</div>
          </div>
          <div className="coupon-manage-header4 pretendard">
            <div>쿠폰 발행일</div>
          </div>
          <div className="coupon-manage-header4 pretendard">
            <div>쿠폰 마감일</div>
          </div>
          <div className="coupon-manage-header5 pretendard">
            <div>발행쿠폰 금액</div>
          </div>
          <div className="coupon-manage-header6 pretendard">
            <div>발급인원</div>
          </div>
          <div className="coupon-manage-header6 pretendard">
            <div>수령인원</div>
          </div>
        </div>
        <div className="coupon-manage-list">
          {this.state.coupons.map((coupon: CouponInfo, index: number) => (
            <CouponManageItem
              title={coupon.title}
              type={coupon.remainCount === 0 ? "비활성" : "활성"}
              lectureId={coupon.lectureId}
              issueDate={coupon.issueDate}
              closeDate={coupon.closeDate}
              discountPrice={coupon.discountPrice}
              chargeCount={coupon.chargeCount}
              receivePeople={coupon.receivePeople}
              remainCount={coupon.remainCount}
              checked={coupon.checked}
              onClick={() => {
                const coupons = this.state.coupons;
                coupons[index].checked = !coupons[index].checked;
                this.setState({ coupons });
              }}
            />
          ))}
        </div>
      </div>
    );
  }
}
