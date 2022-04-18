import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, View} from 'react-native';
import {Button} from 'react-native-elements';

interface GroupViewHeaderProps {
  editMode: boolean;
  setEditMode: (editMode: boolean) => void;
}

export default class GroupViewHeader extends React.Component<
  GroupViewHeaderProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <Text style={this.styles.text}>그룹</Text>
        <View style={this.styles.buttonWrapper}>
          <Button
            buttonStyle={this.styles.button}
            titleStyle={this.styles.title}
            title={this.props.editMode ? '완료' : '편집'}
            onPress={() => {
              this.props.setEditMode(!this.props.editMode);
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      marginTop: STATUSBAR_HEIGHT,
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      paddingHorizontal: 15,
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
    },
    buttonWrapper: {
      flexDirection: 'row',
    },
    button: {backgroundColor: 'white'},
    title: {color: '#4a4a4c'},
  });
}
