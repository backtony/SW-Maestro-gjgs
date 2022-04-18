import * as React from 'react';
import {StyleSheet, Text, View, ScrollView} from 'react-native';
import {Button} from 'react-native-elements';
import Category from '../../utils/Category';

class CategorySelect extends React.Component<{}, {categoryIdList: number[]}> {
  constructor(props: any) {
    super(props);
    this.state = {categoryIdList: []};
  }
  componentDidMount() {
    this.setState({
      categoryIdList: this.props.route.params.categoryIdList,
    });
  }

  private onClickedSubButton(id: string) {
    let updatedSubList = [...this.state.categoryIdList, +id];

    if (this.state.categoryIdList.includes(+id)) {
      updatedSubList = updatedSubList.filter(value => value !== +id);
    }

    this.setState({categoryIdList: updatedSubList});
  }

  render() {
    return (
      <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
        <ScrollView style={this.styles.scrollview}>
          {Category.getCategoryList().map(value => (
            <View>
              <Text style={this.styles.header}>{value}</Text>
              <View style={this.styles.subButtonContainer}>
                {Category.getSubCategoryList(value).map(value2 => (
                  <Button
                    title={value2.sub}
                    onPress={() => this.onClickedSubButton(value2.id)}
                    key={value2.id}
                    buttonStyle={
                      this.state.categoryIdList.includes(+value2.id)
                        ? this.styles.subButtonClicked
                        : this.styles.subButton
                    }
                  />
                ))}
              </View>
            </View>
          ))}
        </ScrollView>

        <View style={this.styles.bottomButtonContainer}>
          <Button
            title="변경"
            style={this.styles.bottomButton}
            onPress={() => {
              this.props.route.params.onChangeCategory(
                this.state.categoryIdList,
              );
              this.props.navigation.goBack();
            }}
          />
          <Button
            title="취소"
            style={this.styles.bottomButton}
            onPress={() => {
              this.props.navigation.goBack();
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    scrollview: {
      width: '100%',
    },
    flatList: {
      width: '100%',
      padding: 10,
    },
    block: {
      height: 50,
      width: '100%',
      backgroundColor: '#5d5d67',
      marginTop: 10,
      borderRadius: 10,
      justifyContent: 'center',
      alignItems: 'center',
    },

    header: {
      fontSize: 25,
    },
    input: {
      height: 40,
      width: '100%',
      margin: 12,
      borderWidth: 1,
    },
    subButtonContainer: {
      flexDirection: 'row',
      flexWrap: 'wrap',
    },
    subButton: {marginTop: 5, marginLeft: 5, backgroundColor: '#3498db'},
    subButtonClicked: {marginTop: 5, marginLeft: 5, backgroundColor: '#2980b9'},
    bottomButtonContainer: {
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'space-around',
    },
    bottomButton: {width: '100%'},
  });
}

export default CategorySelect;
