import GeneralButton from '@/components/GeneralButton';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

interface BulletinEditViewTimeProps {
  onClickTime: (time: string) => void;
  onClickTimeBig: () => void;
  time: string[];
}

export default class BulletinEditViewTime extends React.Component<
  BulletinEditViewTimeProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>가능한 시간</Text>
        <View style={{flexDirection: 'row'}}>
          <GeneralButton
            title={'오전'}
            onClick={() => {
              this.props.onClickTime('MORNING');
            }}
            clicked={this.props.time.includes('MORNING')}
          />
          <GeneralButton
            title={'오후'}
            onClick={() => {
              this.props.onClickTime('NOON');
            }}
            clicked={this.props.time.includes('NOON')}
          />
          <GeneralButton
            title={'저녁'}
            onClick={() => {
              this.props.onClickTime('AFTERNOON');
            }}
            clicked={this.props.time.includes('AFTERNOON')}
          />
          <GeneralButton
            title={'무관'}
            onClick={() => {
              this.props.onClickTimeBig();
            }}
            clicked={(() => {
              return (
                JSON.stringify(this.props.time.sort()) ===
                JSON.stringify(['AFTERNOON', 'MORNIING', 'NOON'])
              );
            })()}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 40,
      marginBottom: 10,
    },
  });
}
