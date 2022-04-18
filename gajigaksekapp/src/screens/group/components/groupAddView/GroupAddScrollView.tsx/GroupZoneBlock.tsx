import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, View} from 'react-native';
import {Button} from 'react-native-elements';

interface GroupZoneBlockProps {
  modalVisible: boolean;
  subZone: string;
  setModalVisible: (modalVisible: boolean) => void;
}

export default class GroupZoneBlock extends React.Component<
  GroupZoneBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <View style={this.styles.container}>
          <Text style={this.styles.text}>위치</Text>
          <Button
            buttonStyle={this.styles.button}
            titleStyle={this.styles.buttonTitle}
            onPress={() => this.props.setModalVisible(!this.props.modalVisible)}
            title={'편집'}
          />
        </View>
        <View style={this.styles.subZoneWrapper}>
          <Text style={this.styles.subZoneText}>{this.props.subZone}</Text>
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    text: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      marginTop: 20,
      marginBottom: 10,
    },
    button: {
      width: 43,
      height: 28,
      borderRadius: 6,
      backgroundColor: 'transparent',
      paddingTop: 8,
      paddingBottom: 5,
    },
    buttonTitle: {
      color: '#4f6cff',
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
    },
    subZoneWrapper: {
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      height: 44,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    subZoneText: {
      width: '70%',
      paddingLeft: 13,
    },
  });
}
